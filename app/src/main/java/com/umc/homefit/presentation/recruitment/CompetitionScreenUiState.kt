package com.umc.homefit.presentation.recruitment

import com.umc.homefit.data.dto.CompetitionDto
import com.umc.homefit.data.dto.RecruitmentDto

sealed interface CompetitionScreenUiState {
    data object Loading : CompetitionScreenUiState
    data class Success(
        val recruitment: RecruitmentDto,   // 헤더(제목/태그/탭)용
        val competition: CompetitionDto    // 경쟁률 데이터 전체
    ) : CompetitionScreenUiState
    data class Error(val message: String) : CompetitionScreenUiState
}
