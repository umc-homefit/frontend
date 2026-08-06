package com.umc.homefit.domain.model.home

enum class FeaturedNoticeStatus {
    RECRUITING,
    SCHEDULED,
    CLOSING_SOON,
    CLOSED,
    UNKNOWN
}

data class FeaturedNotice(
    val noticeId: Long,
    val title: String,
    val region: String,
    val district: String?,
    val unitSummary: String?,
    val depositMin: Long?,
    val depositMax: Long?,
    val monthlyRentMin: Long?,
    val monthlyRentMax: Long?,
    val status: FeaturedNoticeStatus,
    val statusDisplayText: String,
    val isAdditionalRecruitment: Boolean,
    val applicationStartAt: String?,
    val applicationEndAt: String?,
    val dDayText: String?,
    val views: Int,
    val interestedCount: Int,
    val isSaved: Boolean
)
