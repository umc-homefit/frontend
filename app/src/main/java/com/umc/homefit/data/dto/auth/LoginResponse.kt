package com.umc.homefit.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val isNewUser: Boolean,
    val userId: Long
)
