package com.umc.homefit.presentation.finance

sealed interface RecommendedProductScreenUiState {
    object Loading : RecommendedProductScreenUiState
    data class Success(val data: String) : RecommendedProductScreenUiState
    data class Error(val message: String) : RecommendedProductScreenUiState
}
