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
        // Initialize with success default state
        _uiState.value = NotificationSettingScreenUiState.Success("Data initialized for NotificationSettingScreen")
    }
}

