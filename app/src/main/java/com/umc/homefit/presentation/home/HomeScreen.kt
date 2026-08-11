package com.umc.homefit.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.presentation.component.TopBarAction
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.recruitment.component.NoticeCard

@Suppress("UNUSED_PARAMETER")
@Composable
fun HomeScreenRoute(
    viewModel: HomeScreenViewModel,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAllAnnouncementClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onAnalysisClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onGuideClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()

    HomeScreen(
        userName = userName,
        uiState = uiState,
        hasNotifications = true,
        onNotificationClick = onNotificationClick,
        onSearchClick = onSearchClick,
        onAllAnnouncementClick = onAllAnnouncementClick,
        onFavoriteClick = onFavoriteClick,
        onAnalysisClick = onAnalysisClick,
        onFinanceClick = onFinanceClick,
        onGuideClick = onGuideClick,
        onNavigateToDetail = onNavigateToDetail,
        onToggleBookmark = viewModel::toggleBookmark,
        onRetry = viewModel::loadFeaturedNotices,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    userName: String?,
    uiState: HomeScreenUiState,
    hasNotifications: Boolean,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAllAnnouncementClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onToggleBookmark: (Long) -> Unit,
    onRetry: () -> Unit,
    onAnalysisClick: () -> Unit,
    onFinanceClick: () -> Unit,
    onGuideClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        modifier = modifier,
        title = null,
        titleContent = {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_homefit_logo
                ),
                contentDescription = "HomeFit",
                modifier = Modifier
                    .offset(x = (-4).dp)
                    .width(130.dp)
                    .height(48.dp),
                contentScale = ContentScale.Fit
            )
        },
        showBackButton = false,
        actions = listOf(
            TopBarAction(
                icon = painterResource(
                    id = if (hasNotifications) {
                        R.drawable.ic_notification_red
                    } else {
                        R.drawable.ic_notification
                    }
                ),
                contentDescription = if (hasNotifications) {
                    "새 알림 있음"
                } else {
                    "알림"
                },
                onClick = onNotificationClick,
                iconSize = 32.dp,
                iconScale = 1.6f
            )
        )
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF)),
            contentPadding = PaddingValues(
                bottom = 18.dp
            )
        ) {
            item {
                HomeHeaderSection(
                    userName = userName,
                    onSearchClick = onSearchClick,
                    onAllAnnouncementClick = onAllAnnouncementClick
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .background(Color(0xFFF0F4F9))
                )
            }

            item {
                HomeMenuSection(
                    onFavoriteClick = onFavoriteClick,
                    onAnalysisClick = onAnalysisClick,
                    onFinanceClick = onFinanceClick,
                    onGuideClick = onGuideClick
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .background(Color(0xFFF0F4F9))
                )
            }

            item {
                Text(
                    text = "주요 공고",
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 20.dp,
                        end = 16.dp,
                        bottom = 12.dp
                    ),
                    color = Color(0xFF18191B),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            when (uiState) {
                HomeScreenUiState.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is HomeScreenUiState.Error -> item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.message,
                            color = Color(0xFF9298A2),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "다시 시도",
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .clickable(onClick = onRetry),
                            color = Color(0xFF3C45F3),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                is HomeScreenUiState.Success -> {
                    if (uiState.notices.isEmpty()) {
                        item {
                            Text(
                                text = "조회된 주요 공고가 없습니다",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                color = Color(0xFF9298A2),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(
                            items = uiState.notices,
                            key = NoticeDto::noticeId
                        ) { notice ->
                            NoticeCard(
                                notice = notice,
                                onClick = {
                                    onNavigateToDetail(notice.noticeId.toString())
                                },
                                onToggleBookmark = {
                                    onToggleBookmark(notice.noticeId)
                                },
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeaderSection(
    userName: String?,
    onSearchClick: () -> Unit,
    onAllAnnouncementClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 24.dp,
                end = 16.dp,
                bottom = 18.dp
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 92.dp)
            ) {
                Text(
                    text = if (userName.isNullOrBlank()) {
                        "맞춤 공고"
                    } else {
                        "${userName}님을 위한 맞춤 공고"
                    },
                    color = Color(0xFF18191B),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "내 조건에 맞는 청약 정보를 확인하세요",
                    color = Color(0xFF9298A2),
                    fontSize = 12.sp
                )
            }

            Image(
                painter = painterResource(
                    id = R.drawable.ic_homefi_notice
                ),
                contentDescription = null,
                modifier = Modifier
                    .offset(y = 15.dp)
                    .align(Alignment.BottomEnd)
                    .size(60.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        HomeSearchBar(
            onClick = onSearchClick
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        AllAnnouncementButton(
            onClick = onAllAnnouncementClick
        )
    }
}

@Composable
private fun HomeSearchBar(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF4F5F8))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "공고명, 지하철역명, 단지명 등으로 검색",
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            color = Color(0xFF9298A2),
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC3C45F3),
                            Color(0x663C45F3)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.size(27.dp)
            )
        }
    }
}

@Composable
private fun AllAnnouncementButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF3C45F3),
                        Color(0x803C45F3)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "전체 공고 보기",
            color = Color(0xFFFFFFFF),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HomeMenuSection(
    onFavoriteClick: () -> Unit,
    onAnalysisClick: () -> Unit,
    onFinanceClick: () -> Unit,
    onGuideClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp,
                vertical = 13.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        HomeMenuItem(
            iconRes = R.drawable.ic_favorite,
            label = "관심 공고",
            onClick = onFavoriteClick,
            modifier = Modifier.weight(1f)
        )

        HomeMenuItem(
            iconRes = R.drawable.ic_analysis,
            label = "분석 기록",
            onClick = onAnalysisClick,
            modifier = Modifier.weight(1f)
        )

        HomeMenuItem(
            iconRes = R.drawable.ic_finance,
            label = "금융 상품",
            onClick = onFinanceClick,
            modifier = Modifier.weight(1f)
        )

        HomeMenuItem(
            iconRes = R.drawable.ic_guide,
            label = "청약 가이드",
            onClick = onGuideClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HomeMenuItem(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = iconRes
            ),
            contentDescription = label,
            modifier = Modifier.size(32.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = Color(0xFF919AA4),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


private val sampleNotices = listOf(
    NoticeDto(
        noticeId = 1,
        title = "강동구 청년안심주택 추가모집",
        announcementNo = "2026-강동-003",
        region = "서울",
        district = "강동구",
        unitSummary = "전용 24㎡",
        depositMin = 32_000_000,
        depositMax = 48_000_000,
        monthlyRentMin = 280_000,
        monthlyRentMax = 410_000,
        status = "CLOSING_SOON",
        statusDisplayText = "마감임박",
        isAdditionalRecruitment = true,
        applicationStartAt = "2026-07-01T10:00:00Z",
        applicationEndAt = "2026-07-10T18:00:00Z",
        dDayText = "D-3",
        views = 120,
        interestedCount = 32,
        isSaved = false
    )
)

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        userName = "길동",
        uiState = HomeScreenUiState.Success(sampleNotices),
        hasNotifications = true,
        onNotificationClick = {},
        onSearchClick = {},
        onAllAnnouncementClick = {},
        onFavoriteClick = {},
        onToggleBookmark = {},
        onRetry = {},
        onAnalysisClick = {},
        onFinanceClick = {},
        onGuideClick = {},
        onNavigateToDetail = {}
    )
}
