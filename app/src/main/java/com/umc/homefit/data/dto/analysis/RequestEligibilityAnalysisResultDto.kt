package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

@Serializable
data class RequestEligibilityAnalysisResultDto(
    val analysisId: Long,
    val resultLevel: String,
    val eligibilityScore: Int,
    val shortageAmount: Long,
    val rentBurdenRate: Double,
    val summaryMessage: String? = null,
    val conditionResults: List<EligibilityConditionResultDto>,
    val analyzedAt: String
)
