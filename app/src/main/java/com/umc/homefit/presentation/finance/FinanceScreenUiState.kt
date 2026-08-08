package com.umc.homefit.presentation.finance

sealed interface FinanceScreenUiState {
    object Loading : FinanceScreenUiState
    data class Success(
        val matchedCount: String,
        val minRate: String,
        val maxLimitAmount: String,
        val products: List<FinanceRecommendedProductUiModel>
    ) : FinanceScreenUiState
    data object ConditionProfileRequired : FinanceScreenUiState
    data class Error(val message: String) : FinanceScreenUiState
}

data class FinanceRecommendedProductUiModel(
    val productId: Long,
    val title: String,
    val productType: String,
    val interestRate: String,
    val amountDescription: String,
    val targetDescription: String,
    val tags: List<String>,
    val providerLogoUrl: String? = null
)
