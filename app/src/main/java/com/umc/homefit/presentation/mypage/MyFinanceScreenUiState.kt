package com.umc.homefit.presentation.mypage

import com.umc.homefit.presentation.analysis.FinanceInfoSection

sealed interface MyFinanceScreenUiState {
    object Loading : MyFinanceScreenUiState
    data class Success(val sections: List<FinanceInfoSection>) : MyFinanceScreenUiState
    data class Error(val message: String) : MyFinanceScreenUiState
}
