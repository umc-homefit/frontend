package com.umc.homefit.data.repository.finance

import com.umc.homefit.data.api.finance.ConditionProfileApiService
import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequest
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.finance.ConditionProfileRepository
import javax.inject.Inject

class ConditionProfileRepositoryImpl @Inject constructor(
    private val conditionProfileApiService: ConditionProfileApiService
) : ConditionProfileRepository {

    override suspend fun updateConditionProfile(
        request: UpdateConditionProfileRequest
    ): NetworkResult<UpdateConditionProfileResponse> =
        safeApiCall { conditionProfileApiService.updateConditionProfile(request) }

    override suspend fun getConditionProfile(): NetworkResult<ConditionProfileResponse> =
        safeApiCall { conditionProfileApiService.getConditionProfile() }
}
