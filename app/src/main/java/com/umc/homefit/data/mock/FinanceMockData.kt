package com.umc.homefit.data.mock

import com.umc.homefit.R
import com.umc.homefit.data.dto.RecommendedProductDto

object FinanceMockData {

    val recommendedProducts = listOf(

        RecommendedProductDto(
            id = "1",
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
            id = "2",
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
            id = "3",
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
            id = "4",
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
            id = "5",
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
            id = "6",
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
    )
}
