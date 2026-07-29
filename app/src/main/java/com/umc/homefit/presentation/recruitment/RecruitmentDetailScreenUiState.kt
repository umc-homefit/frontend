package com.umc.homefit.presentation.recruitment

import com.umc.homefit.data.dto.recruitment.RecruitmentDto

sealed interface RecruitmentDetailScreenUiState {
    object Loading : RecruitmentDetailScreenUiState
    data class Success(val recruitment: RecruitmentDto) : RecruitmentDetailScreenUiState
    data class Error(val message: String) : RecruitmentDetailScreenUiState
}
