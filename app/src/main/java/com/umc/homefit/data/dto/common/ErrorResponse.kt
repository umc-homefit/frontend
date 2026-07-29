package com.umc.homefit.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String
)
