package com.umc.homefit.data.dto.recruitment

import kotlinx.serialization.Serializable

/**
 * GET /api/notices/{noticeId} 응답의 result 필드
 * 입주 분석 결과 화면 "산정 기준"(전용 면적)에서 쓰는 필드만 우선 반영
 * (공급 유형은 EligibilityAnalysisResultDto.supplyType으로 이동, 여기선 더 이상 안 씀)
 */
@Serializable
data class NoticeDetailResponse(
    val noticeId: Long,
    val units: List<NoticeUnitSummary> = emptyList()
)

@Serializable
data class NoticeUnitSummary(
    val unitId: Long,
    val exclusiveAreaM2: Double? = null
)
