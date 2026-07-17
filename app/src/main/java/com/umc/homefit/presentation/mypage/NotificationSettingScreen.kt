package com.umc.homefit.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.ui.component.AppScaffold
import com.umc.homefit.R

private val CardBorderColor = Color(0xFFD2D9E2)
private val ToggleOnColor = Color(0xFF34C759)

private data class NotificationInfoItem(
    val label: String,
    val iconRes: Int
)

private val notificationInfoItems = listOf(
    NotificationInfoItem("신규 공고", R.drawable.ic_noti_home),
    NotificationInfoItem("관심 공고 변동", R.drawable.ic_noti_idea),
    NotificationInfoItem("청약 일정", R.drawable.ic_noti_calender),
    NotificationInfoItem("금융 상품 업데이트", R.drawable.ic_noti_card)
)

@Composable
fun NotificationSettingScreenRoute(
    viewModel: NotificationSettingScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    NotificationSettingScreen(
        uiState = uiState,
        onBack = onBack,
        onTogglePush = viewModel::onTogglePush,
        onToggleSms = viewModel::onToggleSms,
        modifier = modifier
    )
}

@Composable
fun NotificationSettingScreen(
    uiState: NotificationSettingScreenUiState,
    onBack: () -> Unit,
    onTogglePush: (Boolean) -> Unit = {},
    onToggleSms: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = "알림 설정",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is NotificationSettingScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NotificationSettingScreenUiState.Success -> {
                    NotificationSettingContent(
                        pushEnabled = uiState.pushEnabled,
                        smsEnabled = uiState.smsEnabled,
                        onTogglePush = onTogglePush,
                        onToggleSms = onToggleSms,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is NotificationSettingScreenUiState.Error -> {
                    Text(text = "Error: ${uiState.message}", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun NotificationSettingContent(
    pushEnabled: Boolean,
    smsEnabled: Boolean,
    onTogglePush: (Boolean) -> Unit,
    onToggleSms: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        NotificationToggleCard(
            label = "PUSH 알림",
            checked = pushEnabled,
            onCheckedChange = onTogglePush,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        NotificationToggleCard(
            label = "SMS 알림",
            checked = smsEnabled,
            onCheckedChange = onToggleSms,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "알림 수신 내용",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        notificationInfoItems.forEachIndexed { index, item ->
            NotificationInfoCard(
                item = item,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            if (index != notificationInfoItems.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun NotificationToggleCard(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = ToggleOnColor,
                checkedThumbColor = Color.White
            )
        )
    }
}

@Composable
private fun NotificationInfoCard(
    item: NotificationInfoItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSettingScreenPreview() {
    NotificationSettingScreen(
        uiState = NotificationSettingScreenUiState.Success(
            pushEnabled = true,
            smsEnabled = false
        ),
        onBack = {}
    )
}
