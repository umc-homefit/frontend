package com.umc.homefit.presentation.mypage

sealed interface MyFinanceScreenUiState {
    object Loading : MyFinanceScreenUiState
    data class Success(val sections: List<FinanceInfoSection>) : MyFinanceScreenUiState
    data class Error(val message: String) : MyFinanceScreenUiState
}

data class FinanceInfoSection(
    val title: String,
    val rows: List<FinanceInfoRow>
)

data class FinanceInfoRow(
    val label: String,
    val value: String? = null
)
