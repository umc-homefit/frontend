package com.umc.homefit.domain.repository.notification

import com.umc.homefit.data.dto.notification.NotificationListResponse
import com.umc.homefit.data.dto.notification.ReadNotificationResponse
import com.umc.homefit.data.remote.NetworkResult

interface NotificationRepository {
    suspend fun getNotifications(
        page: Int = 0,
        size: Int = 20
    ): NetworkResult<NotificationListResponse>

    suspend fun markNotificationAsRead(
        notificationId: Long
    ): NetworkResult<ReadNotificationResponse>
}
