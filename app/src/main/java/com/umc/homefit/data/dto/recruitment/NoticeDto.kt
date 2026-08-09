package com.umc.homefit.data.dto.recruitment

import com.umc.homefit.data.dto.common.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class NoticeListResponse(
    val notices: List<NoticeDto>,
    val pageInfo: PageInfo
)

@Serializable
data class NoticeDto(
    val noticeId: Long,
    val title: String,
    val announcementNo: String? = null,
    val region: String,
    val district: String?,
    val unitSummary: String?,
    val depositMin: Long?,
    val depositMax: Long?,
    val monthlyRentMin: Long?,
    val monthlyRentMax: Long?,
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
