package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.repository.RecruitmentRepository
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

    private var allRecruitments: List<RecruitmentDto> = emptyList()
    private var currentFilter: FilterState? = null

    init {
        loadRecruitments()
    }

    private fun loadRecruitments() {
        viewModelScope.launch {
            _uiState.value = try {
                allRecruitments = recruitmentRepository.getRecruitments()
                RecruitmentListScreenUiState.Success(filterRecruitments())
            } catch (e: Exception) {
                RecruitmentListScreenUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    fun toggleBookmark(id: String) {
        allRecruitments = allRecruitments.map { recruitment ->
            if (recruitment.id == id) {
                recruitment.copy(isBookmarked = !recruitment.isBookmarked)
            } else {
                recruitment
            }
        }
        val currentState = _uiState.value
        if (currentState is RecruitmentListScreenUiState.Success) {
            _uiState.value = currentState.copy(recruitments = filterRecruitments())
        }
    }

    fun applyFilter(filter: FilterState) {
        currentFilter = filter
        val currentState = _uiState.value
        if (currentState is RecruitmentListScreenUiState.Success) {
            _uiState.value = currentState.copy(recruitments = filterRecruitments())
        }
    }

    private fun filterRecruitments(): List<RecruitmentDto> {
        val filter = currentFilter ?: return allRecruitments
        return allRecruitments.filter { recruitment ->
            (filter.selectedDistrict == "전체" || recruitment.location.contains(filter.selectedDistrict)) &&
                matchesArea(recruitment, filter) &&
                matchesDeposit(recruitment, filter)
        }
    }

    // 슬라이더가 표시 가능한 최댓값(SLIDER_MAX_FILTER의 기본값)까지 밀린 상태면 그 이상 전부 포함
    private fun matchesArea(recruitment: RecruitmentDto, filter: FilterState): Boolean {
        val minOk = recruitment.area >= filter.minArea.toDouble()
        val maxOk = filter.maxArea >= SLIDER_MAX_FILTER.maxArea || recruitment.area <= filter.maxArea.toDouble()
        return minOk && maxOk
    }

    private fun matchesDeposit(recruitment: RecruitmentDto, filter: FilterState): Boolean {
        val filterMinWon = filter.minDeposit.toWon()
        val filterMaxWon = filter.maxDeposit.toWon()

        // 공고의 보증금 범위(depositMin~depositMax)와 필터 범위가 겹치는지 확인
        val minOk = recruitment.depositMax >= filterMinWon
        val maxOk = filter.maxDeposit >= SLIDER_MAX_FILTER.maxDeposit || recruitment.depositMin <= filterMaxWon

        return minOk && maxOk
    }

    // FilterState의 보증금 단위는 만 원이라 원 단위인 RecruitmentDto.deposit과 비교하려면 변환이 필요함
    private fun Float.toWon(): Long = (this * 10_000).toLong()

    private companion object {
        // FilterState()의 기본 min/maxArea, min/maxDeposit이 곧 슬라이더가 표시하는 전체 범위의 상한이라 이를 그대로 기준으로 사용
        val SLIDER_MAX_FILTER = FilterState()
    }
}
