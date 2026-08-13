package com.umc.homefit.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.recruitment.component.RecruitmentSearchBar

@Composable
fun RecruitmentSearchScreenRoute(
    viewModel: RecruitmentSearchScreenViewModel,
    onBack: () -> Unit,
    onSearchComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecruitmentSearchScreen(
        uiState = uiState,
        onBack = onBack,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onDeleteRecentSearch = viewModel::deleteRecentSearch,
        onSearch = { keyword ->
            val trimmedKeyword = keyword.trim()

            if (trimmedKeyword.isNotBlank()) {
                viewModel.addRecentSearch(trimmedKeyword)
                onSearchComplete(trimmedKeyword)
            }
        },
        modifier = modifier
    )
}

@Composable
fun RecruitmentSearchScreen(
    uiState: RecruitmentSearchScreenUiState,
    onBack: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDeleteRecentSearch: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = "공고 검색",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        when (uiState) {
            RecruitmentSearchScreenUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFFFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF3C45F3)
                    )
                }
            }

            is RecruitmentSearchScreenUiState.Success -> {
                RecruitmentSearchContent(
                    searchQuery = uiState.searchQuery,
                    recentSearches = uiState.recentSearches,
                    popularSearches = uiState.popularSearches,
                    onSearchQueryChange = onSearchQueryChange,
                    onDeleteRecentSearch = onDeleteRecentSearch,
                    onSearch = onSearch,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is RecruitmentSearchScreenUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFFFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = Color(0xFF919AA4),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun RecruitmentSearchContent(
    searchQuery: String,
    recentSearches: List<String>,
    popularSearches: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onDeleteRecentSearch: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFFFFFFF))
            .padding(top = 14.dp)
    ) {
        RecruitmentSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "최근 검색어",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFF161616),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (recentSearches.isEmpty()) {
            Text(
                text = "최근 검색어가 없습니다.",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF919AA4),
                fontSize = 16.sp
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = recentSearches,
                    key = { keyword ->
                        keyword
                    }
                ) { keyword ->
                    RecruitmentRecentSearchChip(
                        keyword = keyword,
                        onClick = {
                            onSearchQueryChange(keyword)
                            onSearch(keyword)
                        },
                        onDeleteClick = {
                            onDeleteRecentSearch(keyword)
                        }
                    )
                }
            }
        }

        // 인기 검색어: 실제 통계가 아니라 목데이터라 노출하지 않는다.
        // 백엔드 API가 생기면 이 자리에 popularSearches 렌더링을 다시 추가하면 된다.
    }
}

@Composable
private fun RecruitmentRecentSearchChip(
    keyword: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color(0xFFFFFFFF),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFDCE2E9)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                top = 5.dp,
                bottom = 5.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = keyword,
                color = Color(0xFF919AA4),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(5.dp))

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "$keyword 삭제",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF919AA4)
                )
            }
        }
    }
}

@Composable
private fun RecruitmentPopularSearchItem(
    rank: Int,
    keyword: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rank.toString(),
            color = Color(0xFF4A4F55),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = keyword,
            color = Color(0xFF4A4F55),
            fontSize = 14.sp
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun RecruitmentSearchScreenPreview() {
    RecruitmentSearchScreen(
        uiState = RecruitmentSearchScreenUiState.Success(
            searchQuery = "",
            recentSearches = listOf(
                "청년안심주택",
                "천호동",
                "행복주택",
                "장기전세"
            ),
            popularSearches = listOf(
                "청년안심주택",
                "행복주택",
                "역세권",
                "강동구",
                "관악구"
            )
        ),
        onBack = {},
        onSearchQueryChange = {},
        onDeleteRecentSearch = {},
        onSearch = {}
    )
}
