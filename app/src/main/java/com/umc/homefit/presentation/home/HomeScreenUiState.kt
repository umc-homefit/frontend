package com.umc.homefit.presentation.home

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState

    data class Success(
        val notices: List<HomeNoticeUiModel>
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}

data class HomeNoticeUiModel(
    val noticeId: Long,
    val title: String,
    val location: String,
    val unitSummary: String,
    val deposit: String,
    val applicationPeriod: String,
    val status: HomeNoticeStatus,
    val statusDisplayText: String,
    val dDayText: String?,
    val isSaved: Boolean
)

enum class HomeNoticeStatus {
    RECRUITING,
    SCHEDULED,
    CLOSING_SOON,
    CLOSED,
    UNKNOWN
}
