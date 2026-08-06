package com.umc.homefit.data.api.finance

import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequest
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileResponse
import com.umc.homefit.data.dto.common.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ConditionProfileApiService {

    @PUT("users/me/condition-profile")
    suspend fun updateConditionProfile(
        @Body request: UpdateConditionProfileRequest
    ): BaseResponse<UpdateConditionProfileResponse>

    @GET("users/me/condition-profile")
    suspend fun getConditionProfile(): BaseResponse<ConditionProfileResponse>
}
