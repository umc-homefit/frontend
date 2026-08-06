package com.umc.homefit.domain.repository.finance

import com.umc.homefit.data.dto.analysis.ConditionProfileResultDto
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequestDto
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileResultDto
import com.umc.homefit.data.remote.NetworkResult

interface ConditionProfileRepository {
    suspend fun updateConditionProfile(
        request: UpdateConditionProfileRequestDto
    ): NetworkResult<UpdateConditionProfileResultDto>

    suspend fun getConditionProfile(): NetworkResult<ConditionProfileResultDto>
}
