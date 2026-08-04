package com.umc.homefit.data.dto.analysis

import com.umc.homefit.data.dto.common.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class MyEligibilityAnalysesResultDto(
    val analyses: List<EligibilityAnalysisHistoryItemDto>,
    val pageInfo: PageInfo
)

@Serializable
data class EligibilityAnalysisHistoryItemDto(
    val analysisId: Long,
    val noticeId: Long,
    val unitId: Long,
    val noticeTitle: String,
    val announcementNo: String? = null,
    val unitName: String? = null,
    val exclusiveAreaM2: Double? = null,
    val expectedDepositAmount: Long,
    val applicationStartAt: String? = null,
    val applicationEndAt: String? = null,
    val noticeStatus: String,
    val noticeStatusDisplayText: String,
    val isAdditionalRecruitment: Boolean,
    val resultLevel: String,
    val eligibilityScore: Int,
    val shortageAmount: Long,
    val rentBurdenRate: Double,
    val analyzedAt: String,
    val competitionRate: String? = null
)
