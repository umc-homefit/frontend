package com.umc.homefit.presentation.finance

import com.umc.homefit.data.dto.finance.FinanceProductCategory
import com.umc.homefit.data.dto.finance.FinanceProviderType
import com.umc.homefit.data.dto.finance.LoanProductResponse
import com.umc.homefit.util.toWonText

internal fun LoanProductResponse.toFinanceRecommendedProductUiModel(): FinanceRecommendedProductUiModel =
    FinanceRecommendedProductUiModel(
        productId = productId,
        title = productName,
        providerLogoUrl = providerLogoUrl,
        productType = providerType.toLabel(),
        interestRate = rateRange.toAnnualRateText(),
        amountDescription = maxLimitAmount?.let { "대출한도 | 최대 ${it.toWonText()}" }
            ?: "대출한도 | 상품별 상이",
        targetDescription = maxIncome?.let { "연소득 | ${it.toWonText()} 이하" }
            ?: "제공기관 | $providerName",
        tags = buildList {
            add(productCategory.toCategoryLabel())
            if (requireNoHouse) add("무주택자")
            if (firstTimeBuyerOnly == true) add("생애최초")
            if (incomeTaxDeductible == true) add("소득공제")
        }.distinct()
    )

internal fun FinanceProviderType.toLabel(): String = when (this) {
    FinanceProviderType.POLICY -> "정부지원"
    FinanceProviderType.BANK -> "은행상품"
    FinanceProviderType.UNKNOWN -> "기타"
}

private fun FinanceProductCategory.toCategoryLabel(): String = when (this) {
    FinanceProductCategory.MORTGAGE_LOAN -> "주택담보대출"
    FinanceProductCategory.JEONSE_LOAN -> "전세대출"
    FinanceProductCategory.SUBSCRIPTION_SAVINGS -> "청약저축"
    FinanceProductCategory.UNKNOWN -> "기타"
}

internal fun String.toAnnualRateText(): String =
    if (this == "정보 없음" || startsWith("연 ")) this else "연 $this"
