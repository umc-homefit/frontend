package com.umc.homefit.presentation.finance

import com.umc.homefit.data.dto.finance.FinanceProductDto

sealed interface RecommendedProductScreenUiState {

    data object Loading : RecommendedProductScreenUiState

    data class Success(
        val products: List<FinanceProductDto>
    ) : RecommendedProductScreenUiState

    data class Error(
        val message: String
    ) : RecommendedProductScreenUiState
}
