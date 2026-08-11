package com.umc.homefit.data.dto.finance

import kotlinx.serialization.Serializable

@Serializable
data class LoanProductDetailResponse(
    val productId: Long,
    val productName: String,
    val providerType: FinanceProviderType = FinanceProviderType.UNKNOWN,
    val productCategory: FinanceProductCategory? = null,
    val providerName: String,
    val providerLogoUrl: String? = null,
    val rateRange: String? = null,
    val maxIncome: Long? = null,
    val firstTimeBuyerOnly: Boolean? = null,
    val incomeTaxDeductible: Boolean? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val requireNoHouse: Boolean? = null,
    val maxLimitAmount: Long? = null,
    val ltvRatio: Int? = null,
    val dtiRatio: Int? = null,
    val loanTermMinYears: Int? = null,
    val loanTermMaxYears: Int? = null,
    val preferentialRateDiscount: Double? = null,
    val firstTimeBuyerRateDiscount: Double? = null,
    val minMonthlyDeposit: Long? = null,
    val maxMonthlyDeposit: Long? = null,
    val officialUrl: String? = null,
    val description: String? = null
)
