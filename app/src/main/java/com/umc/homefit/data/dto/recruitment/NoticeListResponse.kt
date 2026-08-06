package com.umc.homefit.data.dto.recruitment

import com.umc.homefit.data.dto.common.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class NoticeListResponse(
    val notices: List<NoticeSummaryResponse>,
    val pageInfo: PageInfo
)

@Serializable
data class NoticeSummaryResponse(
    val noticeId: Long,
    val title: String,
    val region: String,
    val district: String? = null,
    val unitSummary: String? = null,
    val depositMin: Long? = null,
    val depositMax: Long? = null,
    val monthlyRentMin: Long? = null,
    val monthlyRentMax: Long? = null,
    val status: String,
    val statusDisplayText: String,
    val isAdditionalRecruitment: Boolean,
    val applicationStartAt: String? = null,
    val applicationEndAt: String? = null,
    val dDayText: String? = null,
    val views: Int,
    val interestedCount: Int,
    val isSaved: Boolean
)
