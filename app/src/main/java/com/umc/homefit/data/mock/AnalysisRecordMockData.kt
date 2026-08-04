package com.umc.homefit.data.mock

import com.umc.homefit.data.dto.analysis.EligibilityAnalysisHistoryItemDto
import com.umc.homefit.data.dto.analysis.MyEligibilityAnalysesResultDto
import com.umc.homefit.data.dto.common.PageInfo

object AnalysisRecordMockData {

    fun getRecords(): MyEligibilityAnalysesResultDto {
        val analyses = listOf(
            EligibilityAnalysisHistoryItemDto(
                analysisId = 1L,
                noticeId = 101L,
                unitId = 1001L,
                noticeTitle = "강동구 청년안심주택 2025-03호",
                announcementNo = "2024-강동-031",
                unitName = "전용 59㎡ A타입",
                exclusiveAreaM2 = 59.0,
                expectedDepositAmount = 32_000_000L,
                applicationStartAt = "2026-07-04T16:00:00.000Z",
                applicationEndAt = "2026-07-07T15:00:00.000Z",
                noticeStatus = "RECRUITING",
                noticeStatusDisplayText = "모집중",
                isAdditionalRecruitment = false,
                resultLevel = "HIGH",
                eligibilityScore = 82,
                shortageAmount = 0L,
                rentBurdenRate = 18.5,
                analyzedAt = "2026-07-07T10:23:00Z",
                competitionRate = "12:1"
            ),
            EligibilityAnalysisHistoryItemDto(
                analysisId = 2L,
                noticeId = 102L,
                unitId = 1002L,
                noticeTitle = "강동구 고덕강일 청년안심주택",
                announcementNo = "2024-강동-045",
                unitName = "전용 36㎡ B타입",
                exclusiveAreaM2 = 36.0,
                expectedDepositAmount = 18_000_000L,
                applicationStartAt = "2026-07-19T15:00:00.000Z",
                applicationEndAt = "2026-07-24T15:00:00.000Z",
                noticeStatus = "SCHEDULED",
                noticeStatusDisplayText = "예정",
                isAdditionalRecruitment = false,
                resultLevel = "MEDIUM",
                eligibilityScore = 61,
                shortageAmount = 3_500_000L,
                rentBurdenRate = 27.2,
                analyzedAt = "2026-07-01T09:10:00Z"
            ),
            EligibilityAnalysisHistoryItemDto(
                analysisId = 3L,
                noticeId = 103L,
                unitId = 1003L,
                noticeTitle = "송파구 신혼희망타운 3단지",
                announcementNo = "2024-송파-012",
                unitName = null,
                exclusiveAreaM2 = null,
                expectedDepositAmount = 65_000_000L,
                applicationStartAt = null,
                applicationEndAt = null,
                noticeStatus = "CLOSED",
                noticeStatusDisplayText = "마감",
                isAdditionalRecruitment = false,
                resultLevel = "NOT_ELIGIBLE",
                eligibilityScore = 24,
                shortageAmount = 12_000_000L,
                rentBurdenRate = 41.0,
                analyzedAt = "2026-06-20T14:45:00Z"
            ),
            EligibilityAnalysisHistoryItemDto(
                analysisId = 4L,
                noticeId = 104L,
                unitId = 1004L,
                noticeTitle = "인천 연수구 국민임대주택",
                announcementNo = "2024-연수-022",
                unitName = "전용 49㎡",
                exclusiveAreaM2 = 49.0,
                expectedDepositAmount = 15_000_000L,
                applicationStartAt = "2026-06-30T15:00:00.000Z",
                applicationEndAt = "2026-07-04T15:00:00.000Z",
                noticeStatus = "CLOSING_SOON",
                noticeStatusDisplayText = "마감임박",
                isAdditionalRecruitment = true,
                resultLevel = "NEED_CHECK",
                eligibilityScore = 55,
                shortageAmount = 0L,
                rentBurdenRate = 22.8,
                analyzedAt = "2026-06-28T08:00:00Z"
            )
        )

        return MyEligibilityAnalysesResultDto(
            analyses = analyses,
            pageInfo = PageInfo(
                page = 0,
                size = 10,
                totalElements = analyses.size.toLong(),
                totalPages = 1,
                hasNext = false
            )
        )
    }
}
