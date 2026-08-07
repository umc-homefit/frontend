package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.ConditionProfileRepository
import com.umc.homefit.presentation.analysis.emptyFinanceInfoSections
import com.umc.homefit.presentation.analysis.toFinanceInfoSections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFinanceScreenViewModel @Inject constructor(
    private val conditionProfileRepository: ConditionProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyFinanceScreenUiState>(MyFinanceScreenUiState.Loading)
    val uiState: StateFlow<MyFinanceScreenUiState> = _uiState.asStateFlow()

    init {
        loadConditionProfile()
    }

    fun loadConditionProfile() {
        viewModelScope.launch {
            val sections = when (val result = conditionProfileRepository.getConditionProfile()) {
                is NetworkResult.Success -> result.data.toFinanceInfoSections()
                is NetworkResult.Error -> emptyFinanceInfoSections()
            }
            _uiState.value = MyFinanceScreenUiState.Success(sections = sections)
        }
    }
}
