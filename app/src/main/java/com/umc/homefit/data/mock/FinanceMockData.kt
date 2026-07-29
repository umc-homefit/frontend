package com.umc.homefit.data.mock

import com.umc.homefit.R
import com.umc.homefit.data.dto.home.RecommendedProductDto

object FinanceMockData {

    val recommendedProducts = listOf(
        RecommendedProductDto(
            productId = 101,
            title = "디딤돌 대출",
            iconRes = R.drawable.img_kookmin_logo,
            productType = "정부지원",
            interestRate = "연 2.15% ~ 3.00%",
            amountDescription = "대출한도 | 최대 2억 5천만 원",
            targetDescription = "연소득 | 6천만 원 이하",
            tags = listOf(
                "무주택자",
                "생애최초"
            )
        ),
        RecommendedProductDto(
            productId = 102,
            title = "버팀목 전세대출",
            iconRes = R.drawable.img_hana_logo,
            productType = "정부지원",
            interestRate = "연 2.10% ~ 2.90%",
            amountDescription = "대출한도 | 최대 1억 2천만 원",
            targetDescription = "연소득 | 5천만 원 이하",
            tags = listOf(
                "무주택자",
                "청년"
            )
        ),
        RecommendedProductDto(
            productId = 103,
            title = "주택청약종합저축",
            iconRes = R.drawable.img_shinhan_logo,
            productType = "정부지원",
            interestRate = "연 최대 4.50%",
            amountDescription = "월 납입 | 최대 50만 원",
            targetDescription = "가입대상 | 무주택 청년",
            tags = listOf(
                "청약",
                "소득공제"
            )
        ),
        RecommendedProductDto(
            productId = 104,
            title = "디딤돌 대출",
            iconRes = R.drawable.img_kookmin_logo,
            productType = "정부지원",
            interestRate = "연 2.15% ~ 3.00%",
            amountDescription = "대출한도 | 최대 2억 5천만 원",
            targetDescription = "연소득 | 6천만 원 이하",
            tags = listOf(
                "무주택자",
                "생애최초"
            )
        ),
        RecommendedProductDto(
            productId = 105,
            title = "버팀목 전세대출",
            iconRes = R.drawable.img_hana_logo,
            productType = "정부지원",
            interestRate = "연 2.10% ~ 2.90%",
            amountDescription = "대출한도 | 최대 1억 2천만 원",
            targetDescription = "연소득 | 5천만 원 이하",
            tags = listOf(
                "무주택자",
                "청년"
            )
        ),
        RecommendedProductDto(
            productId = 106,
            title = "주택청약종합저축",
            iconRes = R.drawable.img_shinhan_logo,
            productType = "정부지원",
            interestRate = "연 최대 4.50%",
            amountDescription = "월 납입 | 최대 50만 원",
            targetDescription = "가입대상 | 무주택 청년",
            tags = listOf(
                "청약",
                "소득공제"
            )
        )
    )
}
