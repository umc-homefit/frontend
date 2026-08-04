package com.umc.homefit.data.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val nickname: String? = null,
    val birthDate: String? = null,
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: String,
    val updatedAt: String
)
