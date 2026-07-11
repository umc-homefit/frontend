package com.umc.homefit.presentation.mypage

sealed interface SavedRecruitmentScreenUiState {
    object Loading : SavedRecruitmentScreenUiState
    data class Success(val data: String) : SavedRecruitmentScreenUiState
    data class Error(val message: String) : SavedRecruitmentScreenUiState
}
