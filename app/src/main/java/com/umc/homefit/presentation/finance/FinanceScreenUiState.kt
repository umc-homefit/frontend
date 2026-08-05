package com.umc.homefit.presentation.finance

import com.umc.homefit.data.dto.finance.FinanceMatchResultDto

sealed interface FinanceScreenUiState {

    data object Loading : FinanceScreenUiState

    data class Success(
        val result: FinanceMatchResultDto
    ) : FinanceScreenUiState

    data class Error(
        val message: String
    ) : FinanceScreenUiState
}
