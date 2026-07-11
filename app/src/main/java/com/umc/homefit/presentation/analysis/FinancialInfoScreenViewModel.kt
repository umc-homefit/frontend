package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FinancialInfoScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<FinancialInfoScreenUiState>(FinancialInfoScreenUiState.Loading)
    val uiState: StateFlow<FinancialInfoScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = FinancialInfoScreenUiState.Success("Data initialized for FinancialInfoScreen")
    }
}

