package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyFinanceScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<MyFinanceScreenUiState>(MyFinanceScreenUiState.Loading)
    val uiState: StateFlow<MyFinanceScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = MyFinanceScreenUiState.Success("Data initialized for MyFinanceScreen")
    }
}

