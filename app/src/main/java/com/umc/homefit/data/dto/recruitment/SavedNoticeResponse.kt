package com.umc.homefit.data.dto.recruitment

import kotlinx.serialization.Serializable

@Serializable
data class SavedNoticeResponse(
    val savedNoticeId: Long,
    val noticeId: Long,
    val title: String,
    val announcementNo: String? = null,
    val region: String,
    val district: String? = null,
    val unitSummary: String? = null,
    val depositMin: Long? = null,
    val depositMax: Long? = null,
    val status: String,
    val statusDisplayText: String,
    val isAdditionalRecruitment: Boolean,
    val applicationStartAt: String? = null,
    val applicationEndAt: String? = null,
    val dDayText: String? = null,
    val interestedCount: Int,
    val savedAt: String
)
