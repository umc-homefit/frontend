package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalysisResultScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<AnalysisResultScreenUiState>(AnalysisResultScreenUiState.Loading)
    val uiState: StateFlow<AnalysisResultScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = AnalysisResultScreenUiState.Success("Data initialized for AnalysisResultScreen")
    }
}

