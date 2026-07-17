package com.umc.homefit.data.dto

import androidx.annotation.DrawableRes

data class RecommendedProductDto(
    val id: String,
    val title: String,
    @DrawableRes val iconRes: Int,
    val productType: String,
    val interestRate: String,
    val amountDescription: String,
    val targetDescription: String,
    val tags: List<String>
)
