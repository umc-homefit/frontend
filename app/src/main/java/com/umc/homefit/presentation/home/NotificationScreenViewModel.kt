package com.umc.homefit.presentation.home


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class NotificationScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState =
        MutableStateFlow<NotificationScreenUiState>(
            NotificationScreenUiState.Success(
                notifications = createMockNotifications()

                    /* notifications = emptyList() */
                    /* 알림이 없을 때 */


            )
        )

    val uiState: StateFlow<NotificationScreenUiState> =
        _uiState.asStateFlow()

    private fun createMockNotifications(): List<NotificationUiModel> {
        return listOf(
            NotificationUiModel(
                id = 1,
                type = NotificationType.NEW_ANNOUNCEMENT,
                title = "신규 공고",
                message = "관심 지역에 새로운 공고가 등록되었습니다.",
                timeText = "방금"
            ),
            NotificationUiModel(
                id = 2,
                type = NotificationType.ANNOUNCEMENT_CHANGED,
                title = "관심 공고 변동",
                message = "관심 공고 정보가 업데이트되었습니다.",
                timeText = "10분 전"
            ),
            NotificationUiModel(
                id = 3,
                type = NotificationType.APPLICATION_SCHEDULE,
                title = "청약 일정",
                message = "관심 공고의 청약 접수가 오늘 시작됩니다.",
                timeText = "20분 전"
            ),
            NotificationUiModel(
                id = 4,
                type = NotificationType.FINANCE_PRODUCT,
                title = "금융 상품",
                message = "청년 전세대출 상품이 새롭게 출시되었습니다.",
                timeText = "40분 전"
            )
        )
    }
}
