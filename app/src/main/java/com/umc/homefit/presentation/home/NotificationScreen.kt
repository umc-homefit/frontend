package com.umc.homefit.presentation.home

import  androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.R
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.TopBarAction

@Composable
fun NotificationScreenRoute(
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationScreenViewModel =
        hiltViewModel()
) {
    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    NotificationScreen(
        uiState = uiState,
        onBack = onBack,
        onSettingsClick = onSettingsClick,
        onNotificationClick = onNotificationClick,
        modifier = modifier
    )
}

@Composable
fun NotificationScreen(
    uiState: NotificationScreenUiState,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        modifier = modifier,
        title = "알림",
        showBackButton = true,
        onBackClick = onBack,
        actions = listOf(
            TopBarAction(
                icon = painterResource(
                    id = R.drawable.ic_settings
                ),
                contentDescription = "알림 설정",
                onClick = onSettingsClick
            )
        ),
        showDivider = true
    ) { innerPadding ->
        when (uiState) {
            NotificationScreenUiState.Loading -> {
                NotificationLoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is NotificationScreenUiState.Success -> {
                if (uiState.notifications.isEmpty()) {
                    NotificationEmptyContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                } else {
                    NotificationListContent(
                        notifications = uiState.notifications,
                        onNotificationClick = onNotificationClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }

            is NotificationScreenUiState.Error -> {
                NotificationErrorContent(
                    message = uiState.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun NotificationEmptyContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFFFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_notification_nothing
                ),
                contentDescription = "알림 없음",
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun NotificationListContent(
    notifications: List<NotificationUiModel>,
    onNotificationClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.background(Color(0xFFFFFFFF)),
        contentPadding = PaddingValues(
            horizontal = 20.dp
        )
    ) {
        items(
            items = notifications,
            key = { notification ->
                notification.id
            }
        ) { notification ->
            NotificationListItem(
                notification = notification,
                onClick = {
                    onNotificationClick(notification.id)
                }
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFD2D9E2)
            )
        }
    }
}

@Composable
private fun NotificationLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFFFFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(36.dp),
            color = Color(0xFF3C45F3),
            strokeWidth = 3.dp
        )
    }
}

@Composable
private fun NotificationListItem(
    notification: NotificationUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 8.dp,
                vertical = 30.dp
            ),
        verticalAlignment = Alignment.Top
    ) { Image(
        painter = painterResource(
            id = notificationTypeIconRes(
                notification.type
            )
        ),
        contentDescription = notification.title,
        modifier = Modifier
            .padding(top = 1.dp)
            .size(24.dp),
        contentScale = ContentScale.Fit
    )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF161616),
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = notification.timeText,
                    color = Color(0xFF919AA4),
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = notification.message,
                color = Color(0xFF161616),
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
@Composable
private fun NotificationErrorContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color(0xFF919AA4),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@DrawableRes
private fun notificationTypeIconRes(
    type: NotificationType
): Int {
    return when (type) {
        NotificationType.NEW_NOTICE ->
            R.drawable.ic_noti_home

        NotificationType.CLOSING_SOON ->
            R.drawable.ic_noti_calender
    }
}
