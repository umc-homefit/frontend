package com.umc.homefit.presentation.mypage

sealed interface NotificationSettingScreenUiState {
    object Loading : NotificationSettingScreenUiState
    data class Success(val data: String) : NotificationSettingScreenUiState
    data class Error(val message: String) : NotificationSettingScreenUiState
}
