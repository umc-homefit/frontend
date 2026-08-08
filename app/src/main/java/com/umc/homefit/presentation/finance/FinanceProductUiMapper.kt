package com.umc.homefit.presentation.finance

import com.umc.homefit.data.dto.finance.FinanceProductCategory
import com.umc.homefit.data.dto.finance.FinanceProviderType
import com.umc.homefit.data.dto.finance.LoanProductResponse
import java.text.NumberFormat
import java.util.Locale

internal fun LoanProductResponse.toFinanceRecommendedProductUiModel(): FinanceRecommendedProductUiModel =
    FinanceRecommendedProductUiModel(
        productId = productId,
        title = productName,
        productType = when (providerType) {
            FinanceProviderType.POLICY -> "정부지원"
            FinanceProviderType.BANK -> "은행"
            FinanceProviderType.UNKNOWN -> "기타"
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

private fun FinanceProductCategory.toCategoryLabel(): String = when (this) {
    FinanceProductCategory.MORTGAGE_LOAN -> "주택담보대출"
    FinanceProductCategory.JEONSE_LOAN -> "전세대출"
    FinanceProductCategory.SUBSCRIPTION_SAVINGS -> "청약저축"
    FinanceProductCategory.UNKNOWN -> "기타"
}

internal fun Long.toKoreanAmount(): String {
    val eok = this / 100_000_000
    val man = (this % 100_000_000) / 10_000
    return when {
        eok > 0 && man > 0 -> "${eok}억 ${NumberFormat.getNumberInstance(Locale.KOREA).format(man)}만 원"
        eok > 0 -> "${eok}억 원"
        else -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(this / 10_000)}만 원"
    }
}
