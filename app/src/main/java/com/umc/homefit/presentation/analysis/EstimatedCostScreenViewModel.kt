package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EstimatedCostScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<EstimatedCostScreenUiState>(EstimatedCostScreenUiState.Loading)
    val uiState: StateFlow<EstimatedCostScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = EstimatedCostScreenUiState.Success("Data initialized for EstimatedCostScreen")
    }
}

