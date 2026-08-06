package com.umc.homefit.data.dto.analysis

import kotlinx.serialization.Serializable

/**
 * PUT /api/users/me/condition-profile 응답의 result 필드.
 * (BaseResponse<UpdateConditionProfileResponse>로 감싸서 사용)
 */
@Serializable
data class UpdateConditionProfileResponse(
    val userConditionProfileId: Long,
    val updatedAt: String
)
