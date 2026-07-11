package com.umc.homefit.presentation.home

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Success(val data: String) : HomeScreenUiState
    data class Error(val message: String) : HomeScreenUiState
}
