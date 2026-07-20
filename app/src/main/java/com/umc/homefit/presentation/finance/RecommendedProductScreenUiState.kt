package com.umc.homefit.presentation.finance

import com.umc.homefit.data.dto.RecommendedProductDto

sealed interface RecommendedProductScreenUiState {

    data object Loading : RecommendedProductScreenUiState

    data class Success(
        val products: List<RecommendedProductDto>
    ) : RecommendedProductScreenUiState

    data class Error(
        val message: String
    ) : RecommendedProductScreenUiState
}
