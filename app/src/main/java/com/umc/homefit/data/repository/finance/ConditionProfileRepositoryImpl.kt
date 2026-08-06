package com.umc.homefit.data.repository.finance

import com.umc.homefit.data.api.finance.ConditionProfileApiService
import com.umc.homefit.data.dto.analysis.ConditionProfileResultDto
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequestDto
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileResultDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.finance.ConditionProfileRepository
import javax.inject.Inject

class ConditionProfileRepositoryImpl @Inject constructor(
    private val conditionProfileApiService: ConditionProfileApiService
) : ConditionProfileRepository {

    override suspend fun updateConditionProfile(
        request: UpdateConditionProfileRequestDto
    ): NetworkResult<UpdateConditionProfileResultDto> =
        safeApiCall { conditionProfileApiService.updateConditionProfile(request) }

    override suspend fun getConditionProfile(): NetworkResult<ConditionProfileResultDto> =
        safeApiCall { conditionProfileApiService.getConditionProfile() }
}
