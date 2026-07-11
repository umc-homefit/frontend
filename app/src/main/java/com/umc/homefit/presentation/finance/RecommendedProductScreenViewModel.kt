package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecommendedProductScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<RecommendedProductScreenUiState>(RecommendedProductScreenUiState.Loading)
    val uiState: StateFlow<RecommendedProductScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = RecommendedProductScreenUiState.Success("Data initialized for RecommendedProductScreen")
    }
}

