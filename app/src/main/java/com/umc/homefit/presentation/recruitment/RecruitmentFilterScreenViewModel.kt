package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.domain.repository.recruitment.DistrictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitmentFilterScreenViewModel @Inject constructor(
    private val districtRepository: DistrictRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecruitmentFilterScreenUiState>(RecruitmentFilterScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentFilterScreenUiState> = _uiState.asStateFlow()

    private val _districts = MutableStateFlow<List<String>>(emptyList())
    val districts: StateFlow<List<String>> = _districts.asStateFlow()

    init {
        _uiState.value = RecruitmentFilterScreenUiState.Success(FilterState())
        viewModelScope.launch {
            _districts.value = districtRepository.getDistricts()
        }
    }

    fun updateDistrict(district: String) {
        updateFilterState { it.copy(selectedDistrict = district) }
    }

    fun updateArea(minArea: Float, maxArea: Float) {
        updateFilterState { it.copy(minArea = minArea, maxArea = maxArea) }
    }

    fun updateDeposit(minDeposit: Float, maxDeposit: Float) {
        updateFilterState { it.copy(minDeposit = minDeposit, maxDeposit = maxDeposit) }
    }

    fun resetFilter() {
        _uiState.value = RecruitmentFilterScreenUiState.Success(FilterState())
    }

    private fun updateFilterState(transform: (FilterState) -> FilterState) {
        val currentState = _uiState.value
        if (currentState is RecruitmentFilterScreenUiState.Success) {
            _uiState.value = currentState.copy(data = transform(currentState.data))
        }
    }
}
