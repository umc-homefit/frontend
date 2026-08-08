package com.umc.homefit.presentation.finance

sealed interface RecommendedProductScreenUiState {

    data object Loading : RecommendedProductScreenUiState

    data class Success(
        val products: List<FinanceRecommendedProductUiModel>
    ) : RecommendedProductScreenUiState

    data object ConditionProfileRequired : RecommendedProductScreenUiState

    data class Error(
        val message: String
    ) : RecommendedProductScreenUiState
}
