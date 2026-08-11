package com.umc.homefit.presentation.recruitment

import com.umc.homefit.presentation.component.NoticeCardUiModel

sealed interface RecruitmentListScreenUiState {
    object Loading : RecruitmentListScreenUiState
    data class Success(val recruitments: List<NoticeCardUiModel>) : RecruitmentListScreenUiState
    data class Error(val message: String) : RecruitmentListScreenUiState
}
