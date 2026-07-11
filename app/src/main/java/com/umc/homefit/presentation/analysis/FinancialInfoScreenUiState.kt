package com.umc.homefit.presentation.analysis

sealed interface FinancialInfoScreenUiState {
    object Loading : FinancialInfoScreenUiState
    data class Success(val data: String) : FinancialInfoScreenUiState
    data class Error(val message: String) : FinancialInfoScreenUiState
}
