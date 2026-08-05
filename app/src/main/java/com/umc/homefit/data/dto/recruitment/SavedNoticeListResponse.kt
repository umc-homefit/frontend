package com.umc.homefit.data.dto.recruitment

import com.umc.homefit.data.dto.common.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class SavedNoticeListResponse(
    val savedNotices: List<SavedNoticeResponse>,
    val pageInfo: PageInfo
)
