package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.finance.LoanProductDetailResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.finance.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailScreenViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductDetailScreenUiState>(
            ProductDetailScreenUiState.Loading
        )

    val uiState: StateFlow<ProductDetailScreenUiState> =
        _uiState.asStateFlow()

    private var loadedProductId: Long? = null

    fun loadProductDetail(
        productId: Long,
        forceRefresh: Boolean = false
    ) {
        if (
            !forceRefresh &&
            loadedProductId == productId &&
            _uiState.value is ProductDetailScreenUiState.Success
        ) {
            return
        }

        loadedProductId = productId

        viewModelScope.launch {
            _uiState.value = ProductDetailScreenUiState.Loading

            _uiState.value = when (
                val detailResult = financeRepository.getLoanProductDetail(productId)
            ) {
                is NetworkResult.Success -> {
                    when (
                        val documentsResult =
                            financeRepository.getLoanProductDocuments(productId)
                    ) {
                        is NetworkResult.Success -> ProductDetailScreenUiState.Success(
                            product = detailResult.data.toProductDetailData(
                                requiredDocuments = documentsResult.data.map { document ->
                                    document.documentName
                                }
                            )
                        )

                        is NetworkResult.Error -> ProductDetailScreenUiState.Error(
                            message = documentsResult.message
                        )
                    }
                }

                is NetworkResult.Error -> ProductDetailScreenUiState.Error(
                    message = detailResult.message
                )
            }
        }
    }

    fun retry() {
        val productId = loadedProductId ?: return

        loadProductDetail(
            productId = productId,
            forceRefresh = true
        )
    }
}

private fun LoanProductDetailResponse.toProductDetailData(
    requiredDocuments: List<String>
): ProductDetailData =
    ProductDetailData(
        productId = productId,
        productName = productName,
        providerType = providerType.toLabel(),
        productCategory = productCategory?.name ?: "UNKNOWN",
        providerName = providerName,
        providerLogoUrl = providerLogoUrl,
        rateRange = rateRange ?: "정보 없음",
        maxIncome = maxIncome,
        firstTimeBuyerOnly = firstTimeBuyerOnly == true,
        incomeTaxDeductible = incomeTaxDeductible == true,
        minAge = minAge,
        maxAge = maxAge,
        requireNoHouse = requireNoHouse == true,
        maxLimitAmount = maxLimitAmount,
        ltvRatio = ltvRatio,
        dtiRatio = dtiRatio,
        loanTermMinYears = loanTermMinYears,
        loanTermMaxYears = loanTermMaxYears,
        preferentialRateDiscount = preferentialRateDiscount,
        firstTimeBuyerRateDiscount = firstTimeBuyerRateDiscount,
        minMonthlyDeposit = minMonthlyDeposit,
        maxMonthlyDeposit = maxMonthlyDeposit,
        officialUrl = officialUrl,
        description = description,
        requiredDocuments = requiredDocuments
    )
