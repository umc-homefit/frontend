package com.umc.homefit.domain.repository.notification

import com.umc.homefit.data.dto.notification.AlertSettingsResponse
import com.umc.homefit.data.dto.notification.UpdateAlertSettingsRequest
import com.umc.homefit.data.remote.NetworkResult

interface AlertSettingsRepository {
    suspend fun getAlertSettings(): NetworkResult<AlertSettingsResponse>
    suspend fun updateAlertSettings(request: UpdateAlertSettingsRequest): NetworkResult<AlertSettingsResponse>
}
