package com.umc.homefit.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class SocialAuthRequest(
    val provider: String,
    val oauthToken: String
)
