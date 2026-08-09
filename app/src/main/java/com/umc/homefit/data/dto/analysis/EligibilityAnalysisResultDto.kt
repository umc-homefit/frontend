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
    val supplyType: String // MVP는 백엔드에서 항상 "청년안심주택"으로 내려옴
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
