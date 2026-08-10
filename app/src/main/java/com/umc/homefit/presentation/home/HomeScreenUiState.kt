package com.umc.homefit.presentation.home

import com.umc.homefit.data.dto.recruitment.NoticeDto

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState

    data class Success(
        val notices: List<NoticeDto>
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}
