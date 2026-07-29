package com.umc.homefit.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean
)
