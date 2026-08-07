package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitmentListScreenViewModel @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecruitmentListScreenUiState>(RecruitmentListScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentListScreenUiState> = _uiState.asStateFlow()

    private var recruitments: List<NoticeDto> = emptyList()

    init {
        loadRecruitments(filter = null)
    }

    fun applyFilter(filter: FilterState) {
        loadRecruitments(filter)
    }

    fun toggleBookmark(noticeId: Long) {
        recruitments = recruitments.map { notice ->
            if (notice.noticeId == noticeId) notice.copy(isSaved = !notice.isSaved) else notice
        }
        val currentState = _uiState.value
        if (currentState is RecruitmentListScreenUiState.Success) {
            _uiState.value = currentState.copy(recruitments = recruitments)
        }
    }

    // district/minArea/maxArea/minDeposit/maxDeposit는 서버 쿼리 파라미터로 전달.
    // NoticeDto에는 area가 숫자로 내려오지 않아(unitSummary 문자열) 클라이언트 재필터링이 불가능해짐에 따라 서버 필터링으로 전환.
    private fun loadRecruitments(filter: FilterState?) {
        viewModelScope.launch {
            _uiState.value = RecruitmentListScreenUiState.Loading

            val district = filter?.selectedDistrict?.takeIf { it != "전체" }
            // 슬라이더가 표시 가능한 최댓값까지 밀린 상태면 상한 없음으로 간주해 파라미터 생략(API 스펙: 생략 시 상한 없음)
            val maxArea = filter?.maxArea?.takeIf { it < SLIDER_MAX_FILTER.maxArea }?.toDouble()
            val maxDeposit = filter?.maxDeposit?.takeIf { it < SLIDER_MAX_FILTER.maxDeposit }?.toWon()

            val result = recruitmentRepository.getRecruitments(
                district = district,
                minArea = filter?.minArea?.toDouble(),
                maxArea = maxArea,
                minDeposit = filter?.minDeposit?.toWon(),
                maxDeposit = maxDeposit
            )

            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    recruitments = result.data.notices
                    RecruitmentListScreenUiState.Success(recruitments)
                }
                is NetworkResult.Error -> RecruitmentListScreenUiState.Error(result.message)
            }
        }
    }

    // FilterState의 보증금 단위는 만 원이라 원 단위인 API 파라미터와 비교하려면 변환이 필요함
    private fun Float.toWon(): Long = (this * 10_000).toLong()

    private companion object {
        // FilterState()의 기본 min/maxArea, min/maxDeposit이 곧 슬라이더가 표시하는 전체 범위의 상한이라 이를 그대로 기준으로 사용
        val SLIDER_MAX_FILTER = FilterState()
    }
}
