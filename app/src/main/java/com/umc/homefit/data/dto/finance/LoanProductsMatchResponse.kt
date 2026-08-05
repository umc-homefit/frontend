package com.umc.homefit.data.dto.finance

import kotlinx.serialization.Serializable

@Serializable
data class LoanProductsMatchResponse(
    val matchedCount: Int,
    val minRate: String,
    val maxLimitAmount: Long,
    val products: List<LoanProductResponse>
)

@Serializable
data class LoanProductResponse(
    val productId: Long,
    val productName: String,
    val providerType: String,
    val productCategory: String,
    val providerName: String,
    val rateRange: String,
    val maxIncome: Long? = null,
    val firstTimeBuyerOnly: Boolean,
    val incomeTaxDeductible: Boolean,
    val maxLimitAmount: Long? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val requireNoHouse: Boolean,
    val minMonthlyDeposit: Long? = null,
    val maxMonthlyDeposit: Long? = null,
    val isEligible: Boolean,
    val ageCheckSkipped: Boolean,
    val householdHeadCheckSkipped: Boolean,
    val marriedCheckSkipped: Boolean,
    val newbornCheckSkipped: Boolean,
    val firstTimeBuyerCheckSkipped: Boolean,
    val ineligibleReasons: List<String> = emptyList()
)
