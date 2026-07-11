package com.umc.homefit.presentation.recruitment

sealed interface RecruitmentFilterScreenUiState {
    object Loading : RecruitmentFilterScreenUiState
    data class Success(val data: String) : RecruitmentFilterScreenUiState
    data class Error(val message: String) : RecruitmentFilterScreenUiState
}
