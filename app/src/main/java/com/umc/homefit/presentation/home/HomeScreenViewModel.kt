package com.umc.homefit.presentation.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _recommendedAnnouncements =
        MutableStateFlow(sampleAnnouncements)

    val recommendedAnnouncements =
        _recommendedAnnouncements.asStateFlow()

    private val _hasUnreadNotifications = MutableStateFlow(false)
    val hasUnreadNotifications: StateFlow<Boolean> =
        _hasUnreadNotifications.asStateFlow()

    fun loadNotificationStatus() {
        viewModelScope.launch {
            when (val result = notificationRepository.getNotifications()) {
                is NetworkResult.Success -> {
                    _hasUnreadNotifications.value =
                        result.data.notifications.any { !it.isRead }
                }

                is NetworkResult.Error -> {
                    _hasUnreadNotifications.value = false
                }
            }
        }
    }

    fun toggleBookmark(recruitmentId: String) {
        _recommendedAnnouncements.update { recruitments ->
            recruitments.map { recruitment ->
                if (recruitment.id == recruitmentId) {
                    recruitment.copy(
                        isBookmarked = !recruitment.isBookmarked
                    )
                } else {
                    recruitment
                }
            }
        }
    }
}
