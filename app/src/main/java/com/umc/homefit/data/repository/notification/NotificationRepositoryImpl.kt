package com.umc.homefit.data.repository.notification

import com.umc.homefit.data.api.notification.NotificationApiService
import com.umc.homefit.data.dto.notification.NotificationListResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiService: NotificationApiService
) : NotificationRepository {
    override suspend fun getNotifications(
        page: Int,
        size: Int
    ): NetworkResult<NotificationListResponse> =
        safeApiCall {
            notificationApiService.getNotifications(
                page = page,
                size = size
            )
        }
}
