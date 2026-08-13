package com.umc.homefit.data.dto.notification

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAlertSettingsRequest(
    val pushEnabled: Boolean,
    val noticeAlertEnabled: Boolean,
    val scheduleAlertEnabled: Boolean,
    val financeAlertEnabled: Boolean,
    val interestedRegion: String? = null
)
