package com.umc.homefit.data.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class BasicInfoResponse(
    val userId: Long,
    val email: String,
    val provider: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
