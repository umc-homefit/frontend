package com.umc.homefit.presentation.analysis

sealed interface AnalysisScreenUiState {
    object Loading : AnalysisScreenUiState
    data class Success(val data: String) : AnalysisScreenUiState
    data class Error(val message: String) : AnalysisScreenUiState
}
