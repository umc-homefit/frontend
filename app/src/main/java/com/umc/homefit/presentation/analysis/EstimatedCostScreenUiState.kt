package com.umc.homefit.presentation.analysis

sealed interface EstimatedCostScreenUiState {
    object Loading : EstimatedCostScreenUiState
    data class Success(val data: String) : EstimatedCostScreenUiState
    data class Error(val message: String) : EstimatedCostScreenUiState
}
