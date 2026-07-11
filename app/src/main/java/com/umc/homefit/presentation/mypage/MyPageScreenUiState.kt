package com.umc.homefit.presentation.mypage

sealed interface MyPageScreenUiState {
    object Loading : MyPageScreenUiState
    data class Success(val data: String) : MyPageScreenUiState
    data class Error(val message: String) : MyPageScreenUiState
}
