package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

/**
 * PUT /api/users/me/condition-profile 요청 바디.
 * 스웨거 상 optional 필드(residenceRegionCode, maritalStatus, employmentStatus 등)는
 * 현재 UI에서 입력받지 않으므로 DTO에 포함하지 않는다. (스웨거 required 필드만 포함)
 */
@Serializable
data class UpdateConditionProfileRequestDto(
    val monthlyIncomeAmount: Long,      // 월 총소득
    val totalAssetAmount: Long,         // 총 보유 자산
    val totalDebtAmount: Long,          // 총 부채 금액
    val monthlyDebtPaymentAmount: Long, // 월 상환액
    val cashSavings: Long,              // 보유 현금
    val isHomeless: Boolean,            // 무주택 여부
    val housingOwnershipStatus: HousingOwnershipStatus // 주택 소유 상태
)

@Serializable
enum class HousingOwnershipStatus {
    HOMELESS,     // 무주택
    FAMILY_OWNED, // 세대원(가족) 소유 — HouseStep 라디오 옵션 중 어느 것에 대응하는지 백엔드 확인 필요
    OWNED         // 주택 보유
}
