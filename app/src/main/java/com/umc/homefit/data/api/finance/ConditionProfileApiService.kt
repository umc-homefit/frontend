package com.umc.homefit.data.api.finance

import com.umc.homefit.data.dto.analysis.ConditionProfileResultDto
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequestDto
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileResultDto
import com.umc.homefit.data.dto.common.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ConditionProfileApiService {

    @PUT("users/me/condition-profile")
    suspend fun updateConditionProfile(
        @Body request: UpdateConditionProfileRequestDto
    ): BaseResponse<UpdateConditionProfileResultDto>

    @GET("users/me/condition-profile")
    suspend fun getConditionProfile(): BaseResponse<ConditionProfileResultDto>
}
