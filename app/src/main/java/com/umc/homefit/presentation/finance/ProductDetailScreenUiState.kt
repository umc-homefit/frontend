package com.umc.homefit.presentation.finance

sealed interface ProductDetailScreenUiState {
    object Loading : ProductDetailScreenUiState
    data class Success(val data: String) : ProductDetailScreenUiState
    data class Error(val message: String) : ProductDetailScreenUiState
}
