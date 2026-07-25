package com.umc.homefit.presentation.home

sealed interface NotificationScreenUiState {

    data object Loading : NotificationScreenUiState

    data class Success(
        val notifications: List<NotificationUiModel>
    ) : NotificationScreenUiState

    data class Error(
        val message: String
    ) : NotificationScreenUiState
}

data class NotificationUiModel(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timeText: String
)

enum class NotificationType {
    NEW_ANNOUNCEMENT,
    ANNOUNCEMENT_CHANGED,
    APPLICATION_SCHEDULE,
    FINANCE_PRODUCT
}
