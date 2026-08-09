package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

@Serializable
data class UpdateConditionProfileResponse(
    val userConditionProfileId: Long,
    val updatedAt: String
)
