package com.umc.homefit.presentation.recruitment

import com.umc.homefit.data.dto.recruitment.NoticeDto

sealed interface RecruitmentListScreenUiState {
    object Loading : RecruitmentListScreenUiState
    data class Success(val recruitments: List<NoticeDto>) : RecruitmentListScreenUiState
    data class Error(val message: String) : RecruitmentListScreenUiState
}
