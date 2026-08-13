package com.umc.homefit.presentation.recruitment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.presentation.component.ErrorStateView
import com.umc.homefit.presentation.component.NoticeCard
import com.umc.homefit.presentation.component.NoticeCardAction
import com.umc.homefit.presentation.component.NoticeCardUiModel
import com.umc.homefit.presentation.component.RefreshOnResume
import com.umc.homefit.presentation.recruitment.component.RecruitmentSearchBar
import com.umc.homefit.presentation.theme.RecruitmentAccent
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray
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

    RefreshOnResume(onResume = viewModel::refresh)

    RecruitmentListScreen(
        uiState = uiState,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToFilter = onNavigateToFilter,
        onNavigateToSearch = onNavigateToSearch,
        onToggleBookmark = viewModel::toggleBookmark,
        onStatusFilterChanged = viewModel::onStatusFilterChanged,
        onRetry = viewModel::retry,
        modifier = modifier,
        initialSearchQuery = initialSearchQuery
    )
}

private data class StatusFilterOption(val label: String, val status: NoticeStatus?)

private val statusFilterOptions = listOf(
    StatusFilterOption("전체", null),
    StatusFilterOption("모집중", NoticeStatus.RECRUITING),
    StatusFilterOption("예정", NoticeStatus.SCHEDULED),
    StatusFilterOption("마감임박", NoticeStatus.CLOSING_SOON)
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
    onRetry: () -> Unit = {},
    initialSearchQuery: String = "",
) {
    var searchQuery by rememberSaveable(initialSearchQuery) {
        mutableStateOf(initialSearchQuery)
    }
    var selectedStatus by remember { mutableStateOf<NoticeStatus?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        RecruitmentSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = {},
            onSearch = {},
            readOnly = true,
            onBarClick = onNavigateToSearch
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(statusFilterOptions) { option ->
                    StatusFilterChip(
                        label = option.label,
                        selected = selectedStatus == option.status,
                        onClick = {
                            selectedStatus = option.status
                            onStatusFilterChanged(option.status?.name)
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
                        .size(37.dp)
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
                if (uiState.recruitments.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "조건에 맞는 공고를 찾지 못했어요",
                                color = Color(0xFF18191B),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "다른 검색어나 필터로 다시 찾아보세요",
                                color = RecruitmentTextGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.recruitments, key = { it.id }) { recruitment ->
                            NoticeCard(
                                uiModel = recruitment,
                                onClick = { onNavigateToDetail(recruitment.id) },
                                action = NoticeCardAction.Bookmark(
                                    isSaved = recruitment.isSaved,
                                    onToggle = { onToggleBookmark(recruitment.id.toLong()) }
                                )
                            )
                        }
                    }
                }
            }

            is RecruitmentListScreenUiState.Error -> {
                ErrorStateView(message = uiState.message, onRetry = onRetry)
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
        shape = RoundedCornerShape(120.dp),
        color = Color.White,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentListScreenPreview() {
    RecruitmentListScreen(
        uiState = RecruitmentListScreenUiState.Success(
            recruitments = listOf(
                NoticeCardUiModel(
                    id = "1",
                    title = "2026년 행복주택 입주자 모집공고",
                    infoLine1 = null,
                    infoLine2 = "전용 39.87㎡  보증금 3,000만원",
                    infoLine3 = "청약접수 | 2026.07.14 ~ 2026.07.18",
                    status = NoticeStatus.RECRUITING,
                    statusLabel = "모집중",
                    isSaved = true,
                    dDayText = "D-4"
                ),
                NoticeCardUiModel(
                    id = "2",
                    title = "청년 매입임대주택 입주자 모집공고",
                    infoLine1 = null,
                    infoLine2 = "전용 29.5㎡  보증금 8,000만원",
                    infoLine3 = "청약접수 | 2026.07.20 ~ 2026.07.25",
                    status = NoticeStatus.SCHEDULED,
                    statusLabel = "예정",
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
