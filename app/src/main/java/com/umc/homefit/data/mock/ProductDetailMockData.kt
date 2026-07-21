package com.umc.homefit.data.mock


import com.umc.homefit.presentation.finance.ProductDetailData

object ProductDetailMockData {

    private val products = listOf(
        ProductDetailData(
            productId = 101,
            productName = "디딤돌 대출",
            providerType = "POLICY",
            productCategory = "MORTGAGE_LOAN",
            providerName = "주택도시기금",
            rateRange = "2.15% ~ 3.00%",
            maxIncome = 60_000_000L,
            firstTimeBuyerOnly = true,
            maxLimitAmount = 500_000_000L,
            ltvRatio = 70,
            dtiRatio = 60,
            loanTermMinYears = 10,
            loanTermMaxYears = 30,
            preferentialRateDiscount = 0.5,
            minMonthlyDeposit = null,
            maxMonthlyDeposit = null,
            officialUrl = "https://nhuf.molit.go.kr",
            description = "무주택 세대주의 주택 구입을 지원하는 정책 금융상품입니다.",
            requiredDocuments = listOf(
                "주민등록등본",
                "소득 및 재직 확인 서류",
                "주택 매매계약서",
                "건물 등기사항전부증명서"
            )
        ),
        ProductDetailData(
            productId = 102,
            productName = "청년 버팀목 전세자금대출",
            providerType = "POLICY",
            productCategory = "JEONSE_LOAN",
            providerName = "주택도시기금",
            rateRange = "1.5% ~ 2.7%",
            maxIncome = 60_000_000L,
            firstTimeBuyerOnly = false,
            maxLimitAmount = 200_000_000L,
            ltvRatio = 70,
            dtiRatio = 60,
            loanTermMinYears = 10,
            loanTermMaxYears = 30,
            preferentialRateDiscount = 0.5,
            minMonthlyDeposit = null,
            maxMonthlyDeposit = null,
            officialUrl = "https://nhuf.molit.go.kr",
            description = "청년층의 주거 안정을 지원하기 위한 전세자금대출 상품입니다.",
            requiredDocuments = listOf(
            "주민등록등본 (최근 3개월 이내)",
            "소득 확인 서류 (건강보험료 납부 확인서 등)",
            "재직 증명서 또는 사업자등록증",
            "임대차계약서 사본",
            "임차주택 등기사항전부증명서"
            )
        )
    )

    fun getProductDetail(
        productId: Long
    ): ProductDetailData? {
        return products.find { product ->
            product.productId == productId
        }
    }
}
