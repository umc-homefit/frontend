package com.umc.homefit.presentation.recruitment

sealed interface CompetitionScreenUiState {
    object Loading : CompetitionScreenUiState
    data class Success(val data: String) : CompetitionScreenUiState
    data class Error(val message: String) : CompetitionScreenUiState
}
