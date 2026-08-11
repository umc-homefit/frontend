package com.umc.homefit.presentation.mypage

import com.umc.homefit.presentation.component.NoticeCardUiModel

sealed interface SavedRecruitmentScreenUiState {
    object Loading : SavedRecruitmentScreenUiState
    data class Success(
        val items: List<NoticeCardUiModel>,
        val sortOption: SortOption = SortOption.LATEST,
        val isLoadingMore: Boolean = false,
        val hasNext: Boolean = false
    ) : SavedRecruitmentScreenUiState
    data class Error(val message: String) : SavedRecruitmentScreenUiState
}

enum class SortOption(val label: String) {
    LATEST("최신순"),
    DEADLINE("마감임박순"),
    RENT_LOW("월세 낮은순"),
    DEPOSIT_LOW("보증금 낮은순")
}
