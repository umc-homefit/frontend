package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

/**
 * GET /api/users/me/condition-profile 응답의 result 필드.
 * 백엔드 스웨거 스키마(ConditionProfileResultDto)는 maritalStatus/householdHeadStatus/newbornBirthDate 등
 * 더 많은 필드를 내려주지만, 현재 UI(FinancialInfoScreen)가 아직 다루지 않는 값들이라
 * 화면 연동 전까지는 실제로 쓰는 필드만 우선 반영한다.
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
