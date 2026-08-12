package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.notification.NotificationResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationScreenUiState>(
        NotificationScreenUiState.Loading
    )
    val uiState: StateFlow<NotificationScreenUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationScreenUiState.Loading
            _uiState.value = when (val result = notificationRepository.getNotifications()) {
                is NetworkResult.Success -> NotificationScreenUiState.Success(
                    notifications = result.data.notifications
                        .asSequence()
                        .filterNot { notification -> notification.isRead }
                        .map { notification -> notification.toUiModel() }
                        .toList()
                )

                is NetworkResult.Error -> NotificationScreenUiState.Error(
                    message = result.message
                )
            }
        }
    }

    fun markNotificationAsRead(notificationId: Long) {
        viewModelScope.launch {
            when (notificationRepository.markNotificationAsRead(notificationId)) {
                is NetworkResult.Success -> {
                    val currentState = _uiState.value
                    if (currentState is NotificationScreenUiState.Success) {
                        _uiState.value = currentState.copy(
                            notifications = currentState.notifications.filterNot {
                                it.id == notificationId
                            }
                        )
                    }
                }

                is NetworkResult.Error -> Unit
            }
        }
    }
}

private fun NotificationResponse.toUiModel(): NotificationUiModel {
    val notificationType = when (type) {
        "NEW_NOTICE" -> NotificationType.NEW_NOTICE
        "CLOSING_SOON" -> NotificationType.CLOSING_SOON
        else -> NotificationType.UNKNOWN
    }

    return NotificationUiModel(
        id = notificationId,
        noticeId = noticeId,
        type = notificationType,
        title = title,
        message = content,
        timeText = createdAt.toElapsedTimeText()
    )
}

private fun String.toElapsedTimeText(
    now: Instant = Instant.now()
): String {
    val createdInstant = runCatching {
        OffsetDateTime.parse(this).toInstant()
    }.recoverCatching {
        LocalDateTime.parse(this)
            .atZone(ZoneId.systemDefault())
            .toInstant()
    }.getOrNull() ?: return "정보 없음"

    val elapsedMinutes = Duration.between(createdInstant, now)
        .toMinutes()
        .coerceAtLeast(0)

    return when {
        elapsedMinutes == 0L -> "방금"
        elapsedMinutes < MINUTES_PER_HOUR -> "${elapsedMinutes}분 전"
        else -> "${elapsedMinutes / MINUTES_PER_HOUR}시간 전"
    }
}

private const val MINUTES_PER_HOUR = 60L
