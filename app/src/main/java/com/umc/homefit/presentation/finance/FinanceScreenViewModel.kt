package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.finance.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FinanceScreenViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FinanceScreenUiState>(
            FinanceScreenUiState.Loading
        )

    val uiState = _uiState.asStateFlow()

    init {
        loadMatchedProducts()
    }

    fun loadMatchedProducts() {
        viewModelScope.launch {
            _uiState.value = FinanceScreenUiState.Loading

            when (
                val result =
                    financeRepository.getMatchedProducts()
            ) {
                is NetworkResult.Success -> {
                    _uiState.value =
                        FinanceScreenUiState.Success(
                            result = result.data
                        )
                }

                is NetworkResult.Error -> {
                    _uiState.value =
                        FinanceScreenUiState.Error(
                            message = result.message
                                ?: "금융상품을 불러오지 못했습니다."
                        )
                }

                else -> Unit
            }
        }
    }
}
