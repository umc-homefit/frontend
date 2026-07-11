package com.umc.homefit.presentation.analysis

sealed interface AnalysisResultScreenUiState {
    object Loading : AnalysisResultScreenUiState
    data class Success(val data: String) : AnalysisResultScreenUiState
    data class Error(val message: String) : AnalysisResultScreenUiState
}
