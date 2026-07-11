package com.umc.homefit.presentation.mypage

sealed interface MyFinanceScreenUiState {
    object Loading : MyFinanceScreenUiState
    data class Success(val data: String) : MyFinanceScreenUiState
    data class Error(val message: String) : MyFinanceScreenUiState
}
