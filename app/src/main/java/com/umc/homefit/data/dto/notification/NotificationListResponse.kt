package com.umc.homefit.data.dto.notification

import com.umc.homefit.data.dto.common.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class NotificationListResponse(
    val notifications: List<NotificationResponse>,
    val pageInfo: PageInfo
)

@Serializable
data class NotificationResponse(
    val notificationId: Long,
    val type: String,
    val title: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: String
)
