package com.umc.homefit.data.dto.notification

import kotlinx.serialization.Serializable

@Serializable
data class ReadNotificationResponse(
    val notificationId: Long,
    val isRead: Boolean
)
