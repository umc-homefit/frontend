package com.umc.homefit.presentation.finance

sealed interface FinanceScreenUiState {
    object Loading : FinanceScreenUiState
    data class Success(val data: String) : FinanceScreenUiState
    data class Error(val message: String) : FinanceScreenUiState
}
