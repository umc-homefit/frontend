package com.umc.homefit.presentation.home

import com.umc.homefit.presentation.component.NoticeCardUiModel

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState

    data class Success(
        val notices: List<NoticeCardUiModel>
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}
