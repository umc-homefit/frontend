package com.umc.homefit.presentation.home

import com.umc.homefit.domain.model.home.FeaturedNoticeStatus

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
    val status: FeaturedNoticeStatus,
    val statusDisplayText: String,
    val dDayText: String?,
    val isSaved: Boolean
)
