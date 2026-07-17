package com.umc.homefit.presentation.recruitment

data class FilterState(
    val selectedDistrict: String = "전체",
    val minArea: Float = 0f,
    val maxArea: Float = 59f,
    val minDeposit: Float = 0f,
    val maxDeposit: Float = 10000f
)

sealed interface RecruitmentFilterScreenUiState {
    object Loading : RecruitmentFilterScreenUiState
    data class Success(val data: FilterState) : RecruitmentFilterScreenUiState
    data class Error(val message: String) : RecruitmentFilterScreenUiState
}
