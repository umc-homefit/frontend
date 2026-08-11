package com.umc.homefit.data.dto.recruitment

import kotlinx.serialization.Serializable

@Serializable
data class NoticeDetailResponse(
    val noticeId: Long,
    val title: String,
    val announcementNo: String? = null,
    val region: String,
    val district: String? = null,
    val address: String? = null,
    val sourceUrl: String,
    val status: String,
    val statusDisplayText: String,
    val isAdditionalRecruitment: Boolean,
    val applicationStartAt: String? = null,
    val applicationEndAt: String? = null,
    val views: Int,
    val interestedCount: Int,
    val isSaved: Boolean,
    val units: List<NoticeUnitDto> = emptyList(),
    val conditions: List<NoticeConditionDto> = emptyList(),
    val files: List<NoticeFileDto> = emptyList()
)

@Serializable
data class NoticeUnitDto(
    val unitId: Long,
    val unitName: String,
    val exclusiveAreaM2: Double? = null,
    val supplyAreaM2: Double? = null,
    val depositMin: Long? = null,
    val depositMax: Long? = null,
    val monthlyRentMin: Long? = null,
    val monthlyRentMax: Long? = null,
    val supplyCount: Int? = null
)

@Serializable
data class NoticeConditionDto(
    val conditionId: Long,
    val targetType: String,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val incomeLimitAmount: Long? = null,
    val incomeLimitText: String? = null,
    val assetLimitAmount: Long? = null,
    val assetLimitText: String? = null,
    val requiresHomeless: Boolean? = null,
    val residenceRequirement: String? = null,
    val rawConditionText: String? = null
)

@Serializable
data class NoticeFileDto(
    val fileId: Long,
    val fileName: String,
    val fileType: String,
    val fileUrl: String,
    val registeredAt: String? = null
)
