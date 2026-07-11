package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecruitmentListScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<RecruitmentListScreenUiState>(RecruitmentListScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentListScreenUiState> = _uiState.asStateFlow()

    init {
        // Initialize with success default state
        _uiState.value = RecruitmentListScreenUiState.Success("Data initialized for RecruitmentListScreen")
    }
}

