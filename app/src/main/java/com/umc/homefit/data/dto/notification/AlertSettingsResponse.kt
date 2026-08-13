package com.umc.homefit.data.dto.notification

import kotlinx.serialization.Serializable

@Serializable
data class AlertSettingsResponse(
    val userId: Long,
    val pushEnabled: Boolean,
    val noticeAlertEnabled: Boolean,
    val scheduleAlertEnabled: Boolean,
    val financeAlertEnabled: Boolean,
    val interestedRegion: String? = null,
    val createdAt: String,
    val updatedAt: String
)
