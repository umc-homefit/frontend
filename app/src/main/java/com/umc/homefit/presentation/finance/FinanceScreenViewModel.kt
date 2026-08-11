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
class FinanceScreenViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<FinanceScreenUiState>(FinanceScreenUiState.Loading)
    val uiState: StateFlow<FinanceScreenUiState> = _uiState.asStateFlow()

    init {
        loadMatchedProducts()
    }

    // 재무 정보 입력 화면에서 복귀했을 때 등, 화면 재진입 시 최신 상태로 갱신
    fun refresh() = loadMatchedProducts(showLoading = false)

    fun loadMatchedProducts(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = FinanceScreenUiState.Loading
            }
            _uiState.value = when (val result = financeRepository.getMatchedLoanProducts()) {
                is NetworkResult.Success -> FinanceScreenUiState.Success(
                    matchedCount = "${result.data.matchedCount}가지",
                    minRate = result.data.minRate?.let { "연 $it" } ?: "-",
                    maxLimitAmount = result.data.maxLimitAmount?.let { "최대 ${it.toKoreanAmount()}" } ?: "-",
                    products = result.data.products
                        .filter { product -> product.isEligible }
                        .take(MAX_VISIBLE_PRODUCTS)
                        .map { product -> product.toFinanceRecommendedProductUiModel() }
                )
                is NetworkResult.Error -> {
                    if (result.errorCode == ErrorCode.FINANCE400) {
                        FinanceScreenUiState.ConditionProfileRequired
                    } else {
                        FinanceScreenUiState.Error(result.message)
                    }
                }
            }
        }
    }

    private companion object {
        const val MAX_VISIBLE_PRODUCTS = 3
    }
}
