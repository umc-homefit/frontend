package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.notification.AlertSettingsResponse
import com.umc.homefit.data.dto.notification.UpdateAlertSettingsRequest
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.notification.AlertSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationSettingScreenViewModel @Inject constructor(
    private val alertSettingsRepository: AlertSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationSettingScreenUiState>(
        NotificationSettingScreenUiState.Loading
    )
    val uiState: StateFlow<NotificationSettingScreenUiState> = _uiState.asStateFlow()

    // 화면에는 안 보이지만 PUT 풀바디 전송에 필요한 나머지 alert 설정값 스냅샷
    private var lastSettings: AlertSettingsResponse? = null

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = NotificationSettingScreenUiState.Loading
            when (val result = alertSettingsRepository.getAlertSettings()) {
                is NetworkResult.Success -> {
                    lastSettings = result.data
                    _uiState.value = NotificationSettingScreenUiState.Success(
                        pushEnabled = result.data.pushEnabled,
                        smsEnabled = false // 서버 미지원 필드, 로컬 전용 (이번 범위 아님)
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.value = NotificationSettingScreenUiState.Error(result.message)
                }
            }
        }
    }

    fun onTogglePush(enabled: Boolean) {
        val current = _uiState.value as? NotificationSettingScreenUiState.Success ?: return
        val snapshot = lastSettings ?: return
        val previousPushEnabled = current.pushEnabled

        _uiState.value = current.copy(pushEnabled = enabled) // 낙관적 업데이트

        viewModelScope.launch {
            val request = UpdateAlertSettingsRequest(
                pushEnabled = enabled,
                noticeAlertEnabled = snapshot.noticeAlertEnabled,
                scheduleAlertEnabled = snapshot.scheduleAlertEnabled,
                financeAlertEnabled = snapshot.financeAlertEnabled,
                interestedRegion = snapshot.interestedRegion
            )

            when (val result = alertSettingsRepository.updateAlertSettings(request)) {
                is NetworkResult.Success -> {
                    lastSettings = result.data
                }

                is NetworkResult.Error -> {
                    val rolledBack = _uiState.value
                    if (rolledBack is NotificationSettingScreenUiState.Success) {
                        _uiState.value = rolledBack.copy(pushEnabled = previousPushEnabled)
                    }
                }
            }
        }
    }

    fun onToggleSms(enabled: Boolean) {
        val current = _uiState.value
        if (current is NotificationSettingScreenUiState.Success) {
            // TODO: 서버에 SMS 알림 설정 필드가 추가되면 API 연동 (현재는 로컬 상태만 유지)
            _uiState.value = current.copy(smsEnabled = enabled)
        }
    }
}
