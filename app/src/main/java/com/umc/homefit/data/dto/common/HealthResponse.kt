package com.umc.homefit.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val message: String
)
