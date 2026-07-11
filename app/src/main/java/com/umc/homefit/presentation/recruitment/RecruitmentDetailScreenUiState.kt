package com.umc.homefit.presentation.recruitment

sealed interface RecruitmentDetailScreenUiState {
    object Loading : RecruitmentDetailScreenUiState
    data class Success(val data: String) : RecruitmentDetailScreenUiState
    data class Error(val message: String) : RecruitmentDetailScreenUiState
}
