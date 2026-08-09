package com.umc.homefit.data.dto.recruitment

import kotlinx.serialization.Serializable

@Serializable
data class SaveNoticeResultResponse(
    val noticeId: Long,
    val isSaved: Boolean,
    val interestedCount: Int
)
