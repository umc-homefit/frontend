package com.umc.homefit.presentation.recruitment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.presentation.recruitment.component.NoticeCard
import com.umc.homefit.presentation.theme.RecruitmentAccent
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray
import com.umc.homefit.presentation.theme.SearchFieldBackground
import com.umc.homefit.R

@Composable
fun RecruitmentListScreenRoute(
    viewModel: RecruitmentListScreenViewModel,
    filterResult: FilterState?,
    onFilterConsumed: () -> Unit,
    onNavigateToFilter: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    initialSearchQuery: String = ""
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(filterResult) {
        filterResult?.let { result ->
            viewModel.applyFilter(result)
            onFilterConsumed()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    RecruitmentListScreen(
        uiState = uiState,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToFilter = onNavigateToFilter,
        onNavigateToSearch = onNavigateToSearch,
        onToggleBookmark = viewModel::toggleBookmark,
        onStatusFilterChanged = viewModel::onStatusFilterChanged,
        modifier = modifier,
        initialSearchQuery = initialSearchQuery
    )
}

private data class StatusFilterOption(val label: String, val status: String?)

private val statusFilterOptions = listOf(
    StatusFilterOption("전체", null),
    StatusFilterOption("모집중", "RECRUITING"),
    StatusFilterOption("예정", "SCHEDULED"),
    StatusFilterOption("마감임박", "CLOSING_SOON")
)

@Composable
fun RecruitmentListScreen(
    uiState: RecruitmentListScreenUiState,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFilter: () -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    onToggleBookmark: (Long) -> Unit,
    onStatusFilterChanged: (String?) -> Unit = {},
    initialSearchQuery: String = "",
) {
    var searchQuery by rememberSaveable(initialSearchQuery) {
        mutableStateOf(initialSearchQuery)
    }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {},
                    modifier = Modifier.fillMaxSize(),
                    readOnly = true,
                    placeholder = {
                        Text(
                            text = "공고명, 지하철역명, 단지명 등으로 검색"
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        bottomStart = 4.dp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SearchFieldBackground,
                        unfocusedContainerColor = SearchFieldBackground,
                        disabledContainerColor = SearchFieldBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = RecruitmentTextGray,
                        unfocusedPlaceholderColor = RecruitmentTextGray
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onNavigateToSearch)
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                RecruitmentAccent.copy(alpha = 0.8f),
                                RecruitmentAccent.copy(alpha = 0.4f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(
                                0f,
                                Float.POSITIVE_INFINITY
                            )
                        ),
                        shape = RoundedCornerShape(
                            topEnd = 4.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .clickable(onClick = onNavigateToSearch),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색 화면으로 이동",
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 20.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                items(statusFilterOptions) { option ->
                    StatusFilterChip(
                        label = option.label,
                        selected = selectedStatus == option.status,
                        onClick = {
                            selectedStatus = option.status
                            onStatusFilterChanged(option.status)
                        }
                    )
                }
            }
            IconButton(
                onClick = onNavigateToFilter,
                modifier = Modifier.size(52.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(width = 1.5.dp, color = RecruitmentBorder, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_recruitment_filter),
                            contentDescription = "필터",
                            modifier = Modifier
                                .width(17.47.dp)
                                .height(19.66.dp)
                        )
                    }
                }
            }
        }

        when (uiState) {
            is RecruitmentListScreenUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RecruitmentListScreenUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.recruitments, key = { it.noticeId }) { recruitment ->
                        NoticeCard(
                            notice = recruitment,
                            onClick = { onNavigateToDetail(recruitment.noticeId.toString()) },
                            onToggleBookmark = { onToggleBookmark(recruitment.noticeId) }
                        )
                    }
                }
            }

            is RecruitmentListScreenUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${uiState.message}")
                }
            }
        }
    }
}

@Composable
private fun StatusFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) RecruitmentAccent else RecruitmentBorder
    val textColor = if (selected) RecruitmentAccent else RecruitmentTextGray

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color.White,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentListScreenPreview() {
    RecruitmentListScreen(
        uiState = RecruitmentListScreenUiState.Success(
            recruitments = listOf(
                NoticeDto(
                    noticeId = 1,
                    title = "2026년 행복주택 입주자 모집공고",
                    region = "서울",
                    district = "강남구",
                    unitSummary = "전용 39.87㎡",
                    depositMin = 30000000,
                    depositMax = 30000000,
                    monthlyRentMin = 350000,
                    monthlyRentMax = 350000,
                    status = "RECRUITING",
                    statusDisplayText = "모집중",
                    isAdditionalRecruitment = false,
                    applicationStartAt = "2026-07-14T10:00:00+09:00",
                    applicationEndAt = "2026-07-18T18:00:00+09:00",
                    dDayText = "D-4",
                    views = 100,
                    interestedCount = 12,
                    isSaved = true
                ),
                NoticeDto(
                    noticeId = 2,
                    title = "청년 매입임대주택 입주자 모집공고",
                    region = "서울",
                    district = "마포구",
                    unitSummary = "전용 29.5㎡",
                    depositMin = 80000000,
                    depositMax = 80000000,
                    monthlyRentMin = 0,
                    monthlyRentMax = 0,
                    status = "SCHEDULED",
                    statusDisplayText = "예정",
                    isAdditionalRecruitment = false,
                    applicationStartAt = "2026-07-20T10:00:00+09:00",
                    applicationEndAt = "2026-07-25T18:00:00+09:00",
                    dDayText = null,
                    views = 40,
                    interestedCount = 3,
                    isSaved = false
                )
            )
        ),
        onNavigateToDetail = {},
        onNavigateToFilter = {},
        onNavigateToSearch = {},
        onToggleBookmark = {},
        onStatusFilterChanged = {},
        initialSearchQuery = "청년"
    )
}
