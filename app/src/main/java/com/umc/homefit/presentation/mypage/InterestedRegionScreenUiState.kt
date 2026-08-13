package com.umc.homefit.presentation.mypage

sealed interface InterestedRegionScreenUiState {
    object Loading : InterestedRegionScreenUiState
    data class Success(
        val regions: List<String> = emptyList(),
        val selectedRegion: String = "전체"
    ) : InterestedRegionScreenUiState
    data class Error(val message: String) : InterestedRegionScreenUiState
}
