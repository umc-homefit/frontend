package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageScreenUiState>(MyPageScreenUiState.Loading)
    val uiState: StateFlow<MyPageScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = MyPageScreenUiState.Success("Data initialized for MyPageScreen")
    }
}

