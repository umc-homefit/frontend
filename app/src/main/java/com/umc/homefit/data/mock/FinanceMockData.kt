package com.umc.homefit.data.mock

import com.umc.homefit.data.dto.finance.FinanceProductDto

object FinanceMockData {

    val recommendedProducts: List<FinanceProductDto> = listOf(
        createFinanceProduct(
            productId = 101L,
            productName = "청년전용 버팀목전세자금",
            rateRange = "1.5% ~ 2.7%",
            maxIncome = 60_000_000L,
            maxLimitAmount = 200_000_000L,
            minAge = 19,
            maxAge = 34
        ),
        createFinanceProduct(
            productId = 102L,
            productName = "신혼부부전용 전세자금",
            rateRange = "1.2% ~ 2.1%",
            maxIncome = 75_000_000L,
            maxLimitAmount = 300_000_000L,
            minAge = null,
            maxAge = null
        ),
        createFinanceProduct(
            productId = 103L,
            productName = "신생아 특례 버팀목전세자금",
            rateRange = "1.0% ~ 1.8%",
            maxIncome = 130_000_000L,
            maxLimitAmount = 500_000_000L,
            minAge = null,
            maxAge = null
        ),
        createFinanceProduct(
            productId = 104L,
            productName = "청년전용 버팀목전세자금",
            rateRange = "1.5% ~ 2.7%",
            maxIncome = 60_000_000L,
            maxLimitAmount = 200_000_000L,
            minAge = 19,
            maxAge = 34
        ),
        createFinanceProduct(
            productId = 105L,
            productName = "신혼부부전용 전세자금",
            rateRange = "1.2% ~ 2.1%",
            maxIncome = 75_000_000L,
            maxLimitAmount = 300_000_000L,
            minAge = null,
            maxAge = null
        ),
        createFinanceProduct(
            productId = 106L,
            productName = "신생아 특례 버팀목전세자금",
            rateRange = "1.0% ~ 1.8%",
            maxIncome = 130_000_000L,
            maxLimitAmount = 500_000_000L,
            minAge = null,
            maxAge = null
        )
    )

    private fun createFinanceProduct(
        productId: Long,
        productName: String,
        rateRange: String,
        maxIncome: Long?,
        maxLimitAmount: Long,
        minAge: Int?,
        maxAge: Int?
    ): FinanceProductDto {
        return FinanceProductDto(
            productId = productId,
            productName = productName,
            providerType = "POLICY",
            productCategory = "JEONSE_LOAN",
            providerName = "주택도시기금",
            rateRange = rateRange,
            maxIncome = maxIncome,
            firstTimeBuyerOnly = false,
            maxLimitAmount = maxLimitAmount,
            minAge = minAge,
            maxAge = maxAge,
            requireNoHouse = true,
            minMonthlyDeposit = null,
            maxMonthlyDeposit = null,
            isEligible = true,
            ageCheckSkipped = false,
            householdHeadCheckSkipped = false,
            marriedCheckSkipped = false,
            newbornCheckSkipped = false,
            firstTimeBuyerCheckSkipped = false,
            ineligibleReasons = emptyList()
        )
    }
}
