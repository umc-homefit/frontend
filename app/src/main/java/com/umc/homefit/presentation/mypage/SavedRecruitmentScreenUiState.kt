package com.umc.homefit.presentation.mypage

sealed interface SavedRecruitmentScreenUiState {
    object Loading : SavedRecruitmentScreenUiState
    data class Success(
        val items: List<SavedRecruitmentItem>,
        val sortOption: SortOption = SortOption.LATEST
    ) : SavedRecruitmentScreenUiState
    data class Error(val message: String) : SavedRecruitmentScreenUiState
}

data class SavedRecruitmentItem(
    val id: String,
    val title: String,
    val noticeNumber: String,
    val exclusiveArea: String,
    val deposit: String,
    val applicationPeriod: String,
    val status: RecruitmentStatus,
    val competitionRate: String
)

enum class RecruitmentStatus(val label: String) {
    SCHEDULED("예정"),
    RECRUITING("모집중")
}

enum class SortOption(val label: String) {
    LATEST("최신순"),
    DEADLINE("마감임박순"),
    RENT_LOW("월세 낮은순"),
    DEPOSIT_LOW("보증금 낮은순")
}
