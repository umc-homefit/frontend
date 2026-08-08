package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.finance.FinanceRepository
import com.umc.homefit.util.error.ErrorCode
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

    fun loadRecommendedProducts(sort: String = DEFAULT_SORT) {
        viewModelScope.launch {
            _uiState.value = RecommendedProductScreenUiState.Loading
            _uiState.value = when (
                val result = financeRepository.getMatchedLoanProducts(sort = sort)
            ) {
                is NetworkResult.Success -> RecommendedProductScreenUiState.Success(
                    products = result.data.products
                        .filter { product -> product.isEligible }
                        .map { product -> product.toFinanceRecommendedProductUiModel() }
                )
                is NetworkResult.Error -> {
                    if (result.errorCode == ErrorCode.FINANCE400) {
                        RecommendedProductScreenUiState.ConditionProfileRequired
                    } else {
                        RecommendedProductScreenUiState.Error(result.message)
                    }
                }
            }
        }
    }

    private companion object {
        const val DEFAULT_SORT = "RECOMMENDED"
    }
}
