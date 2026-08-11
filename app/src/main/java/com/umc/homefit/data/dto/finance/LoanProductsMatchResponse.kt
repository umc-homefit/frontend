package com.umc.homefit.data.dto.finance

import kotlinx.serialization.Serializable

@Serializable
enum class FinanceProviderType {
    POLICY,
    BANK,
    UNKNOWN
}

@Serializable
enum class FinanceProductCategory {
    MORTGAGE_LOAN,
    JEONSE_LOAN,
    SUBSCRIPTION_SAVINGS,
    UNKNOWN
}

@Serializable
data class LoanProductsMatchResponse(
    val matchedCount: Int,
    val minRate: String? = null,
    val maxLimitAmount: Long? = null,
    val products: List<LoanProductResponse>
)

@Serializable
data class LoanProductResponse(
    val productId: Long,
    val productName: String,
    val providerType: FinanceProviderType = FinanceProviderType.UNKNOWN,
    val productCategory: FinanceProductCategory = FinanceProductCategory.UNKNOWN,
    val providerName: String,
    val providerLogoUrl: String? = null,
    val rateRange: String,
    val maxIncome: Long? = null,
    val firstTimeBuyerOnly: Boolean? = null,
    val incomeTaxDeductible: Boolean? = null,
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
