package com.umc.homefit.data.dto.recruitment

import kotlinx.serialization.Serializable

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
