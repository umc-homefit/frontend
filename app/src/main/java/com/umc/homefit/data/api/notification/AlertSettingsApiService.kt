package com.umc.homefit.data.api.notification

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.notification.AlertSettingsResponse
import com.umc.homefit.data.dto.notification.UpdateAlertSettingsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AlertSettingsApiService {

    @GET("users/me/alert-settings")
    suspend fun getAlertSettings(): BaseResponse<AlertSettingsResponse>

    @PUT("users/me/alert-settings")
    suspend fun updateAlertSettings(
        @Body request: UpdateAlertSettingsRequest
    ): BaseResponse<AlertSettingsResponse>
}
