package com.umc.homefit.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.presentation.mypage.component.MyPageMenuItem

private val CardBorderColor = Color(0xFFD2D9E2)
private val SectionDividerColor = Color(0xFFF0F4F9)

@Composable
fun MyPageScreenRoute(
    viewModel: MyPageScreenViewModel,
    onNavigateToSaved: () -> Unit, onNavigateToNotification: () -> Unit, onNavigateToFinance: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    MyPageScreen(
        uiState = uiState,
        onNavigateToSaved = onNavigateToSaved,
        onNavigateToNotification = onNavigateToNotification,
        onNavigateToFinance = onNavigateToFinance,

        modifier = modifier
    )
}

@Composable
fun MyPageScreen(
    uiState: MyPageScreenUiState,
    onNavigateToSaved: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToFinance: () -> Unit,

    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is MyPageScreenUiState.Loading -> CircularProgressIndicator()
            is MyPageScreenUiState.Success -> {
                MyPageContent(
                    profile = uiState.profile,
                    onNavigateToSaved = onNavigateToSaved,
                    onNavigateToNotification = onNavigateToNotification,
                    onNavigateToFinance = onNavigateToFinance,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is MyPageScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Composable
private fun MyPageContent(
    profile: MyPageProfile,
    onNavigateToSaved: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToFinance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(27.dp))

        ProfileCard(
            profile = profile,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(27.dp))

        // 구분 바 (전체 폭 회색 띠)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .background(SectionDividerColor)
        )

        Spacer(modifier = Modifier.height(27.dp))

        MyPageMenuItem(
            label = "관심 공고 관리",
            onClick = onNavigateToSaved,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mypage_heart),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        MyPageMenuItem(
            label = "알림 설정",
            onClick = onNavigateToNotification,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mypage_bell),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        MyPageMenuItem(
            label = "금융 정보 관리",
            onClick = onNavigateToFinance,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mypage_bank),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun ProfileCard(
    profile: MyPageProfile,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6366F1), Color(0xFF60A5FA))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mypage_profileimg),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = profile.nickname,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = profile.email,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(
        uiState = MyPageScreenUiState.Success(
            MyPageProfile(nickname = "홍길동", email = "honggildong@email.com")
        ),
        onNavigateToSaved = {},
        onNavigateToNotification = {},
        onNavigateToFinance = {}
    )
}
