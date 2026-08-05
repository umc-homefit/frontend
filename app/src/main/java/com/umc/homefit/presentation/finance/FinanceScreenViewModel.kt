package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.model.finance.LoanProduct
import com.umc.homefit.domain.repository.finance.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.Locale
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

    fun loadMatchedProducts() {
        viewModelScope.launch {
            _uiState.value = FinanceScreenUiState.Loading
            _uiState.value = when (val result = financeRepository.getMatchedLoanProducts()) {
                is NetworkResult.Success -> FinanceScreenUiState.Success(
                    matchedCount = "${result.data.matchedCount}가지",
                    minRate = "연 ${result.data.minRate}",
                    maxLimitAmount = "최대 ${result.data.maxLimitAmount.toKoreanAmount()}",
                    products = result.data.products.take(MAX_VISIBLE_PRODUCTS).map(LoanProduct::toUiModel)
                )
                is NetworkResult.Error -> FinanceScreenUiState.Error(result.message)
            }
        }
    }

    private companion object {
        const val MAX_VISIBLE_PRODUCTS = 3
    }
}

private fun LoanProduct.toUiModel(): FinanceRecommendedProductUiModel =
    FinanceRecommendedProductUiModel(
        productId = productId,
        title = productName,
        productType = when (providerType) {
            "POLICY" -> "정부지원"
            "BANK" -> "은행"
            else -> providerType
        },
        interestRate = "금리 | $rateRange",
        amountDescription = maxLimitAmount?.let { "대출한도 | 최대 ${it.toKoreanAmount()}" }
            ?: "대출한도 | 상품별 상이",
        targetDescription = maxIncome?.let { "연소득 | ${it.toKoreanAmount()} 이하" }
            ?: "제공기관 | $providerName",
        tags = buildList {
            add(productCategory.toCategoryLabel())
            if (requireNoHouse) add("무주택자")
            if (firstTimeBuyerOnly) add("생애최초")
            if (incomeTaxDeductible) add("소득공제")
        }.distinct()
    )

private fun String.toCategoryLabel(): String = when (this) {
    "MORTGAGE_LOAN" -> "주택담보대출"
    "JEONSE_LOAN" -> "전세대출"
    "SUBSCRIPTION_SAVINGS" -> "청약저축"
    else -> this
}

private fun Long.toKoreanAmount(): String {
    val eok = this / 100_000_000
    val man = (this % 100_000_000) / 10_000
    return when {
        eok > 0 && man > 0 -> "${eok}억 ${NumberFormat.getNumberInstance(Locale.KOREA).format(man)}만 원"
        eok > 0 -> "${eok}억 원"
        else -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(this / 10_000)}만 원"
    }
}
