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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
import com.umc.homefit.presentation.recruitment.component.RecruitmentCard
import com.umc.homefit.ui.theme.RecruitmentAccent
import com.umc.homefit.ui.theme.RecruitmentBorder
import com.umc.homefit.ui.theme.RecruitmentTextGray
import com.umc.homefit.ui.theme.SearchFieldBackground
import com.umc.homefit.R

@Composable
fun RecruitmentListScreenRoute(
    viewModel: RecruitmentListScreenViewModel,
    onNavigateToFilter: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    initialSearchQuery: String = ""
) {
    val uiState by viewModel.uiState.collectAsState()
    RecruitmentListScreen(
        uiState = uiState,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToFilter = onNavigateToFilter,
        onNavigateToSearch = onNavigateToSearch,
        onToggleBookmark = viewModel::toggleBookmark,
        modifier = modifier,
        initialSearchQuery = initialSearchQuery
    )
}

private data class StatusFilterOption(val label: String, val status: RecruitmentStatus?)

private val statusFilterOptions = listOf(
    StatusFilterOption("전체", null),
    StatusFilterOption("모집중", RecruitmentStatus.RECRUITING),
    StatusFilterOption("예정", RecruitmentStatus.SCHEDULED),
    StatusFilterOption("마감임박", RecruitmentStatus.CLOSING_SOON)
)

@Composable
fun RecruitmentListScreen(
    uiState: RecruitmentListScreenUiState,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFilter: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialSearchQuery: String = ""

) {
    var searchQuery by rememberSaveable(initialSearchQuery) {
        mutableStateOf(initialSearchQuery)
    }
    var selectedStatus by remember { mutableStateOf<RecruitmentStatus?>(null) }

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
                        onClick = { selectedStatus = option.status }
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
                            painter = painterResource(id = R.drawable.ic_recruit_filter),
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
                val filteredRecruitments = uiState.recruitments.filter { recruitment ->
                    (selectedStatus == null || recruitment.status == selectedStatus) &&
                        (searchQuery.isBlank() || recruitment.title.contains(searchQuery, ignoreCase = true))
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRecruitments, key = { it.id }) { recruitment ->
                        RecruitmentCard(
                            recruitment = recruitment,
                            onClick = { onNavigateToDetail(recruitment.id) },
                            onToggleBookmark = { onToggleBookmark(recruitment.id) }
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
                RecruitmentDto(
                    id = "1",
                    title = "2026년 행복주택 입주자 모집공고",
                    company = "한국토지주택공사",
                    location = "서울특별시 강남구",
                    rentType = "월세",
                    deposit = 30000000,
                    monthlyRent = 350000,
                    announcementDate = "2026-07-13",
                    announcementNumber = "2026-강남-001",
                    area = 39.87,
                    applicationStartDate = "2026-07-14",
                    applicationEndDate = "2026-07-18",
                    status = RecruitmentStatus.RECRUITING,
                    competitionRate = "12.3:1",
                    isBookmarked = true
                ),
                RecruitmentDto(
                    id = "2",
                    title = "청년 매입임대주택 입주자 모집공고",
                    company = "서울주택도시공사",
                    location = "서울특별시 마포구",
                    rentType = "전세",
                    deposit = 80000000,
                    monthlyRent = 0,
                    announcementDate = "2026-07-10",
                    announcementNumber = "2026-마포-014",
                    area = 29.5,
                    applicationStartDate = "2026-07-20",
                    applicationEndDate = "2026-07-25",
                    status = RecruitmentStatus.SCHEDULED,
                    competitionRate = "-",
                    isBookmarked = false
                )
            )
        ),
        onNavigateToDetail = {},
        onNavigateToFilter = {},
        onNavigateToSearch = {},
        onToggleBookmark = {},
        initialSearchQuery = "청년"
    )
}
