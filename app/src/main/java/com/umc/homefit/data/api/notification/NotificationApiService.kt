package com.umc.homefit.data.api.notification

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.notification.NotificationListResponse
import com.umc.homefit.data.dto.notification.ReadNotificationResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {
    @GET("users/me/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<NotificationListResponse>

    @PATCH("users/me/notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: Long
    ): BaseResponse<ReadNotificationResponse>
}
