package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationSettingScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<NotificationSettingScreenUiState>(NotificationSettingScreenUiState.Loading)
    val uiState: StateFlow<NotificationSettingScreenUiState> = _uiState.asStateFlow()

    init {
        // TODO: 실제 알림 설정 API 연동
        _uiState.value = NotificationSettingScreenUiState.Success()
    }

    fun onTogglePush(enabled: Boolean) {
        val current = _uiState.value
        if (current is NotificationSettingScreenUiState.Success) {
            // TODO: 실제 PUSH 알림 설정 API 연동
            _uiState.value = current.copy(pushEnabled = enabled)
        }
    }

    fun onToggleSms(enabled: Boolean) {
        val current = _uiState.value
        if (current is NotificationSettingScreenUiState.Success) {
            // TODO: 실제 SMS 알림 설정 API 연동
            _uiState.value = current.copy(smsEnabled = enabled)
        }
    }
}
