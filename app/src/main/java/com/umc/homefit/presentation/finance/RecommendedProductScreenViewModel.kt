package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.finance.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RecommendedProductScreenViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecommendedProductScreenUiState>(
        RecommendedProductScreenUiState.Loading
    )
    val uiState: StateFlow<RecommendedProductScreenUiState> = _uiState.asStateFlow()

    init {
        loadRecommendedProducts()
    }

    fun loadRecommendedProducts() {
        viewModelScope.launch {
            _uiState.value = RecommendedProductScreenUiState.Loading
            _uiState.value = when (val result = financeRepository.getMatchedLoanProducts()) {
                is NetworkResult.Success -> RecommendedProductScreenUiState.Success(
                    products = result.data.products
                        .map { product -> product.toFinanceRecommendedProductUiModel() }
                )
                is NetworkResult.Error -> RecommendedProductScreenUiState.Error(result.message)
            }
        }
    }
}
