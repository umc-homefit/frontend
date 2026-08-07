package com.umc.homefit.presentation.analysis

data class ConditionProfileDraft(
    val annualIncomeText: String = "",
    val totalAssetText: String = "",
    val financialAssetText: String = "",
    val totalDebtText: String = "",
    val monthlyRepaymentText: String = "",
    val housingStatus: String? = null
)

sealed interface FinancialInfoScreenUiState {
    object Loading : FinancialInfoScreenUiState

    data class Success(
        val draft: ConditionProfileDraft = ConditionProfileDraft(),
        val isSubmitting: Boolean = false,
        val isSubmitted: Boolean = false
    ) : FinancialInfoScreenUiState

    data class Error(
        val message: String,
        val draft: ConditionProfileDraft
    ) : FinancialInfoScreenUiState
}
