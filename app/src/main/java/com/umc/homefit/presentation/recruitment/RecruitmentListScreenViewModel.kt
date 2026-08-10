package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Job

@HiltViewModel
class RecruitmentListScreenViewModel @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
    private val savedNoticeRepository: SavedNoticeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecruitmentListScreenUiState>(RecruitmentListScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentListScreenUiState> = _uiState.asStateFlow()

    private var recruitments: List<NoticeDto> = emptyList()
    private var currentFilter: FilterState? = null
    private var currentStatus: String? = null
    private var currentKeyword: String? = savedStateHandle.get<String>("searchQuery")?.takeIf { it.isNotBlank() }

    init {
        loadRecruitments()
    }

    fun applyFilter(filter: FilterState) {
        currentFilter = filter
        loadRecruitments()
    }

    fun onStatusFilterChanged(status: String?) {
        currentStatus = status
        loadRecruitments()
    }

    fun toggleBookmark(noticeId: Long) {
        val notice = recruitments.find { it.noticeId == noticeId } ?: return
        val nextSaved = !notice.isSaved
        applySavedState(noticeId, nextSaved)

        viewModelScope.launch {
            val isSuccess = if (nextSaved) {
                savedNoticeRepository.saveNotice(noticeId) is NetworkResult.Success
            } else {
                savedNoticeRepository.unsaveNotice(noticeId) is NetworkResult.Success
            }
            if (!isSuccess) {
                applySavedState(noticeId, !nextSaved)
            }
        }
    }

    private fun applySavedState(noticeId: Long, isSaved: Boolean) {
        recruitments = recruitments.map { notice ->
            if (notice.noticeId == noticeId) notice.copy(isSaved = isSaved) else notice
        }
        val currentState = _uiState.value
        if (currentState is RecruitmentListScreenUiState.Success) {
            _uiState.value = currentState.copy(recruitments = recruitments)
        }
    }

    /**
     * 상세 화면 등 다른 화면에서 찜 상태를 바꾸고 돌아왔을 때 목록에 반영하기 위한 재조회.
     * 로딩 화면을 다시 보여주지 않고, 실패해도 기존 목록을 그대로 유지한다(조용히 무시).
     */
    fun refresh() {
        loadRecruitments(showLoading = false)
    }

    private var loadJob: Job? = null
    private fun loadRecruitments(showLoading: Boolean = true) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showLoading) {
                _uiState.value = RecruitmentListScreenUiState.Loading
            }

            val filter = currentFilter
            val district = filter?.selectedDistrict?.takeIf { it != "전체" }
            // 슬라이더가 표시 가능한 최댓값까지 밀린 상태면 상한 없음으로 간주해 파라미터 생략(API 스펙: 생략 시 상한 없음)
            val maxArea = filter?.maxArea?.takeIf { it < SLIDER_MAX_FILTER.maxArea }?.toDouble()
            val maxDeposit = filter?.maxDeposit?.takeIf { it < SLIDER_MAX_FILTER.maxDeposit }?.toWon()

            val result = recruitmentRepository.getRecruitments(
                keyword = currentKeyword,
                status = currentStatus,
                district = district,
                minArea = filter?.minArea?.toDouble(),
                maxArea = maxArea,
                minDeposit = filter?.minDeposit?.toWon(),
                maxDeposit = maxDeposit
            )

            when (result) {
                is NetworkResult.Success -> {
                    recruitments = result.data.notices
                    _uiState.value = RecruitmentListScreenUiState.Success(recruitments)
                }
                is NetworkResult.Error -> {
                    if (showLoading) {
                        _uiState.value = RecruitmentListScreenUiState.Error(result.message)
                    }
                }
            }
        }
    }

    private fun Float.toWon(): Long = (this * 10_000).toLong()

    private companion object {
        val SLIDER_MAX_FILTER = FilterState()
    }
}
