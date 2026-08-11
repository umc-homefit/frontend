package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

@Serializable
data class EligibilityAnalysisResultDto(
    val analysisId: Long,
    val resultLevel: String,
    val eligibilityScore: Int,
    val shortageAmount: Long,
    val rentBurdenRate: Double,
    val summaryMessage: String? = null,
    val conditionResults: List<EligibilityConditionResultDto>,
    val analyzedAt: String,
    val noticeId: Long,
    val unitId: Long,
    val expectedDepositAmount: Long,
    val expectedMonthlyRentAmount: Long,
    val maintenanceFeeAmount: Long? = null,
    val supplyType: String, // MVP는 백엔드에서 항상 "청년안심주택"으로 내려옴
    val exclusiveAreaM2: Double? = null,
    // 분석 시점의 금융입력정보 스냅샷. 스냅샷을 남기기 전(구) 분석 이력은 null로 내려옴 —
    // 이 경우 "입력 정보"는 GET /users/me/condition-profile(현재값)로 대체하지 않고
    // "분석 당시 입력 정보를 사용할 수 없습니다"로 안내한다.
    val conditionProfileSnapshot: ConditionProfileSnapshotDto? = null
)

// 정확한 필드 형태는 아직 Swagger에 반영되지 않아, GET /users/me/condition-profile
// (ConditionProfileResponse)와 동일하다고 가정하고 매핑함. 실제 응답과 다르면 확인 필요.
@Serializable
data class ConditionProfileSnapshotDto(
    val monthlyIncomeAmount: Long = 0,
    val totalAssetAmount: Long = 0,
    val totalDebtAmount: Long = 0,
    val monthlyDebtPaymentAmount: Long = 0,
    val cashSavings: Long = 0,
    val housingOwnershipStatus: HousingOwnershipStatus? = null
)

@Serializable
data class EligibilityConditionResultDto(
    val conditionCode: String,
    val conditionName: String,
    val requiredValue: String? = null,
    val userValue: String? = null,
    val resultStatus: String,
    val failReason: String? = null
)
