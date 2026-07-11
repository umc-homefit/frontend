package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CompetitionScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<CompetitionScreenUiState>(CompetitionScreenUiState.Loading)
    val uiState: StateFlow<CompetitionScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = CompetitionScreenUiState.Success("Data initialized for CompetitionScreen")
    }
}

