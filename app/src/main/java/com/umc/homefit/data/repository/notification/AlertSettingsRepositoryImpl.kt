package com.umc.homefit.data.repository.notification

import com.umc.homefit.data.api.notification.AlertSettingsApiService
import com.umc.homefit.data.dto.notification.AlertSettingsResponse
import com.umc.homefit.data.dto.notification.UpdateAlertSettingsRequest
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.notification.AlertSettingsRepository
import javax.inject.Inject

class AlertSettingsRepositoryImpl @Inject constructor(
    private val alertSettingsApiService: AlertSettingsApiService
) : AlertSettingsRepository {

    override suspend fun getAlertSettings(): NetworkResult<AlertSettingsResponse> =
        safeApiCall { alertSettingsApiService.getAlertSettings() }

    override suspend fun updateAlertSettings(
        request: UpdateAlertSettingsRequest
    ): NetworkResult<AlertSettingsResponse> =
        safeApiCall { alertSettingsApiService.updateAlertSettings(request) }
}
