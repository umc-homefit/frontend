package com.umc.homefit.domain.repository.analysis

import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequest
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileResponse
import com.umc.homefit.data.remote.NetworkResult

interface ConditionProfileRepository {
    suspend fun updateConditionProfile(
        request: UpdateConditionProfileRequest
    ): NetworkResult<UpdateConditionProfileResponse>

    suspend fun getConditionProfile(): NetworkResult<ConditionProfileResponse>
}
