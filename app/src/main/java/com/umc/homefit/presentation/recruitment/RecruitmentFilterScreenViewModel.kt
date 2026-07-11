package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecruitmentFilterScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<RecruitmentFilterScreenUiState>(RecruitmentFilterScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentFilterScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = RecruitmentFilterScreenUiState.Success("Data initialized for RecruitmentFilterScreen")
    }
}

