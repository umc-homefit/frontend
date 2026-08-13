package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.finance.FinanceRepository
import com.umc.homefit.util.error.ErrorCode
import com.umc.homefit.util.toWonText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FinanceScreenViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<FinanceScreenUiState>(FinanceScreenUiState.Loading)
    val uiState: StateFlow<FinanceScreenUiState> = _uiState.asStateFlow()

    init {
        loadMatchedProducts()
    }

    fun refresh() = loadMatchedProducts(showLoading = false)

    /** 에러 화면의 재시도 버튼용. 로딩 화면을 다시 보여주며 재조회한다. */
    fun retry() = loadMatchedProducts()

    private var loadJob: Job? = null
    fun loadMatchedProducts(showLoading: Boolean = true) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showLoading) {
                _uiState.value = FinanceScreenUiState.Loading
            }
            when (val result = financeRepository.getMatchedLoanProducts()) {
                is NetworkResult.Success -> {
                    val eligibleProducts = result.data.products.filter { product -> product.isEligible }
                    _uiState.value = FinanceScreenUiState.Success(
                        matchedCount = "${eligibleProducts.size}가지",
                        minRate = result.data.minRate?.let { "연 $it" } ?: "-",
                        maxLimitAmount = result.data.maxLimitAmount?.let { "최대 ${it.toWonText()}" } ?: "-",
                        products = eligibleProducts
                            .take(MAX_VISIBLE_PRODUCTS)
                            .map { product -> product.toFinanceRecommendedProductUiModel() }
                    )
                }
                is NetworkResult.Error -> {
                    if (showLoading) {
                        _uiState.value = if (result.errorCode == ErrorCode.FINANCE400) {
                            FinanceScreenUiState.ConditionProfileRequired
                        } else {
                            FinanceScreenUiState.Error(result.message)
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val MAX_VISIBLE_PRODUCTS = 3
    }
}
