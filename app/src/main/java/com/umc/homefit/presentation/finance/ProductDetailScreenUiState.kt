package com.umc.homefit.presentation.finance

import androidx.annotation.DrawableRes

sealed interface ProductDetailScreenUiState {

    data object Loading : ProductDetailScreenUiState

    data class Success(
        val product: ProductDetailData
    ) : ProductDetailScreenUiState

    data class Error(
        val message: String
    ) : ProductDetailScreenUiState
}

data class ProductDetailData(
    val productId: Long,
    val productName: String,
    val providerType: String,
    val productCategory: String,
    val providerName: String,

    @DrawableRes
    val iconRes: Int,

    val rateRange: String,
    val maxIncome: Long?,
    val firstTimeBuyerOnly: Boolean,
    val maxLimitAmount: Long?,
    val ltvRatio: Int?,
    val dtiRatio: Int?,
    val loanTermMinYears: Int?,
    val loanTermMaxYears: Int?,
    val preferentialRateDiscount: Double?,
    val minMonthlyDeposit: Long?,
    val maxMonthlyDeposit: Long?,
    val officialUrl: String?,
    val description: String?,
    val requiredDocuments: List<String> = emptyList()
)
