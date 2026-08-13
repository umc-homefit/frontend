package com.umc.homefit.presentation.mypage

sealed interface NotificationSettingScreenUiState {
    object Loading : NotificationSettingScreenUiState
    data class Success(
        val pushEnabled: Boolean = true,
        val smsEnabled: Boolean = false
    ) : NotificationSettingScreenUiState
    data class Error(val message: String) : NotificationSettingScreenUiState
}
