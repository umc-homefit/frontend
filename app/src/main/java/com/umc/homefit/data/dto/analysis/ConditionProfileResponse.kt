package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

/**
 * GET /api/users/me/condition-profile 응답의 result 필드
 * 현재 FinancialInfoScreen UI에서 쓰이는 필드만 우선 반영
 */
@Serializable
data class ConditionProfileResponse(
    val monthlyIncomeAmount: Long,
    val totalAssetAmount: Long,
    val totalDebtAmount: Long,
    val monthlyDebtPaymentAmount: Long,
    val cashSavings: Long,
    val housingOwnershipStatus: HousingOwnershipStatus,
    val isHomeless: Boolean,
    val createdAt: String,
    val updatedAt: String
)
