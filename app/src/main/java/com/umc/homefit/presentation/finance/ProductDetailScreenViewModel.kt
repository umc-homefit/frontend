package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailScreenUiState>(ProductDetailScreenUiState.Loading)
    val uiState: StateFlow<ProductDetailScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = ProductDetailScreenUiState.Success("Data initialized for ProductDetailScreen")
    }
}

