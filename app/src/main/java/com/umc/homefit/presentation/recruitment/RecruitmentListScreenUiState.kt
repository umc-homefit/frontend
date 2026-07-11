package com.umc.homefit.presentation.recruitment

sealed interface RecruitmentListScreenUiState {
    object Loading : RecruitmentListScreenUiState
    data class Success(val data: String) : RecruitmentListScreenUiState
    data class Error(val message: String) : RecruitmentListScreenUiState
}
