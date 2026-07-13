package com.umc.homefit.presentation.recruitment

import com.umc.homefit.data.dto.RecruitmentDto

sealed interface RecruitmentListScreenUiState {
    object Loading : RecruitmentListScreenUiState
    data class Success(val recruitments: List<RecruitmentDto>) : RecruitmentListScreenUiState
    data class Error(val message: String) : RecruitmentListScreenUiState
}
