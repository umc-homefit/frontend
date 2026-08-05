package com.umc.homefit.domain.model.finance

data class LoanProductsMatch(
    val matchedCount: Int,
    val minRate: String,
    val maxLimitAmount: Long,
    val products: List<LoanProduct>
)

data class LoanProduct(
    val productId: Long,
    val productName: String,
    val providerType: String,
    val productCategory: String,
    val providerName: String,
    val rateRange: String,
    val maxIncome: Long?,
    val firstTimeBuyerOnly: Boolean,
    val incomeTaxDeductible: Boolean,
    val maxLimitAmount: Long?,
    val minAge: Int?,
    val maxAge: Int?,
    val requireNoHouse: Boolean,
    val minMonthlyDeposit: Long?,
    val maxMonthlyDeposit: Long?,
    val isEligible: Boolean,
    val ageCheckSkipped: Boolean,
    val householdHeadCheckSkipped: Boolean,
    val marriedCheckSkipped: Boolean,
    val newbornCheckSkipped: Boolean,
    val firstTimeBuyerCheckSkipped: Boolean,
    val ineligibleReasons: List<String>
)
