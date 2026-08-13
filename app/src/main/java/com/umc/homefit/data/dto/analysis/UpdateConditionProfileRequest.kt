package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

/**
 * PUT /api/users/me/condition-profile 요청 바디
 * 현재 UI에서 입력받는 스웨거의 required 필드만 포함
 */
@Serializable
data class UpdateConditionProfileRequest(
    val monthlyIncomeAmount: Long,      // 월 총소득
    val totalAssetAmount: Long,         // 총 보유 자산
    val totalDebtAmount: Long,          // 총 부채 금액
    val monthlyDebtPaymentAmount: Long, // 월 상환액
    val cashSavings: Long,              // 금융 자산
    val isHomeless: Boolean,            // 무주택 여부
    val housingOwnershipStatus: HousingOwnershipStatus // 주택 소유 상태
)

@Serializable
enum class HousingOwnershipStatus {
    HOMELESS,     // 완전 무주택
    FAMILY_OWNED, // 본인 무주택, 세대원 유주택
    OWNED         // 본인 유주택
}
