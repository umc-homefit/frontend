package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SavedRecruitmentScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<SavedRecruitmentScreenUiState>(SavedRecruitmentScreenUiState.Loading)
    val uiState: StateFlow<SavedRecruitmentScreenUiState> = _uiState.asStateFlow()

    init {
        // TODO: 실제 관심 공고 목록 API 연동
        _uiState.value = SavedRecruitmentScreenUiState.Success(items = mockItems)
    }

    fun onSortOptionSelected(option: SortOption) {
        val current = _uiState.value
        if (current is SavedRecruitmentScreenUiState.Success) {
            // TODO: 실제 정렬은 API 연동 시 서버 파라미터로 처리
            _uiState.value = current.copy(sortOption = option)
        }
    }

    fun onRemoveClick(itemId: String) {
        val current = _uiState.value
        if (current is SavedRecruitmentScreenUiState.Success) {
            // TODO: 실제 관심 공고 해제 API 연동
            _uiState.value = current.copy(items = current.items.filterNot { it.id == itemId })
        }
    }

    companion object {
        private val mockItems = listOf(
            SavedRecruitmentItem(
                id = "1",
                title = "강동구 고덕강일 청년안심주택",
                noticeNumber = "2024-강동-031",
                exclusiveArea = "59㎡",
                deposit = "3,200만원",
                applicationPeriod = "2026.07.05 ~ 2026.07.08",
                status = RecruitmentStatus.SCHEDULED,
                competitionRate = "12:1"
            ),
            SavedRecruitmentItem(
                id = "2",
                title = "강동구 고덕강일 청년안심주택",
                noticeNumber = "2024-강동-031",
                exclusiveArea = "59㎡",
                deposit = "3,200만원",
                applicationPeriod = "2026.07.05 ~ 2026.07.08",
                status = RecruitmentStatus.SCHEDULED,
                competitionRate = "12:1"
            ),
            SavedRecruitmentItem(
                id = "3",
                title = "강동구 고덕강일 청년안심주택",
                noticeNumber = "2024-강동-031",
                exclusiveArea = "59㎡",
                deposit = "3,200만원",
                applicationPeriod = "2026.07.05 ~ 2026.07.08",
                status = RecruitmentStatus.SCHEDULED,
                competitionRate = "12:1"
            ),
            SavedRecruitmentItem(
                id = "4",
                title = "강동구 고덕강일 청년안심주택",
                noticeNumber = "2024-강동-031",
                exclusiveArea = "59㎡",
                deposit = "3,200만원",
                applicationPeriod = "2026.07.05 ~ 2026.07.08",
                status = RecruitmentStatus.SCHEDULED,
                competitionRate = "12:1"
            ),
            SavedRecruitmentItem(
                id = "5",
                title = "강동구 청년안심주택 2025-03호",
                noticeNumber = "2024-강동-031",
                exclusiveArea = "59㎡",
                deposit = "3,200만원",
                applicationPeriod = "2026.07.05 ~ 2026.07.08",
                status = RecruitmentStatus.RECRUITING,
                competitionRate = "12:1"
            )
        )
    }
}
