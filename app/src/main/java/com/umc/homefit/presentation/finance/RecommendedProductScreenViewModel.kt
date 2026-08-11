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

    private var currentSort: String = DEFAULT_SORT
    private var currentCategory: String? = null
    private var currentKeyword: String? = null

    init {
        loadRecommendedProducts()
    }

    fun refresh() = loadRecommendedProducts(
        sort = currentSort,
        category = currentCategory,
        keyword = currentKeyword,
        showLoading = false
    )

    fun loadRecommendedProducts(
        sort: String = DEFAULT_SORT,
        category: String? = null,
        keyword: String? = null,
        showLoading: Boolean = true
    ) {
        currentSort = sort
        currentCategory = category
        currentKeyword = keyword
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = RecommendedProductScreenUiState.Loading
            }
            when (
                val result = financeRepository.getMatchedLoanProducts(
                    productCategory = category,
                    keyword = keyword,
                    sort = sort
                )
            ) {
                is NetworkResult.Success -> {
                    _uiState.value = RecommendedProductScreenUiState.Success(
                        products = result.data.products
                            .filter { product -> product.isEligible }
                            .map { product -> product.toFinanceRecommendedProductUiModel() }
                    )
                }
                is NetworkResult.Error -> {
                    // 백그라운드 갱신(showLoading=false) 실패는 이미 화면에 떠 있는 데이터를 지우지 않도록 무시
                    if (showLoading) {
                        _uiState.value = if (result.errorCode == ErrorCode.FINANCE400) {
                            RecommendedProductScreenUiState.ConditionProfileRequired
                        } else {
                            RecommendedProductScreenUiState.Error(result.message)
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val DEFAULT_SORT = "RECOMMENDED"
    }
}
