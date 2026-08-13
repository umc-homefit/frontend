package com.umc.homefit.presentation.finance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.homefit.presentation.finance.component.RecommendedProductSearchBar
import com.umc.homefit.presentation.component.AppScaffold
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

private val ProductSearchBorder = Color(0xFFDCE2E9)
private val ProductSearchTextGray = Color(0xFF919AA4)

@Composable
fun ProductSearchScreenRoute(
    viewModel: ProductSearchScreenViewModel,
    onBack: () -> Unit,
    onSearchComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    ProductSearchScreen(
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
fun ProductSearchScreen(
    uiState: ProductSearchScreenUiState,
    onBack: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDeleteRecentSearch: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = "금융 상품 검색",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        when (uiState) {
            ProductSearchScreenUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductSearchScreenUiState.Success -> {
                ProductSearchContent(
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

            is ProductSearchScreenUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.message}",
                        color = Color(0xFF24282D)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductSearchContent(
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
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 14.dp)
    ) {
        RecommendedProductSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchClick = {
                val keyword = searchQuery.trim()

                if (keyword.isNotBlank()) {
                    onSearch(keyword)
                }
            },
            readOnly = false
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "최근 검색어",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF161616)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (recentSearches.isEmpty()) {
            Text(
                text = "최근 검색어가 없습니다.",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 16.sp,
                color = ProductSearchTextGray
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = recentSearches,
                    key = { keyword ->
                        keyword
                    }
                ) { keyword ->
                    RecentSearchChip(
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
private fun RecentSearchChip(
    keyword: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = ProductSearchBorder
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
                color = ProductSearchTextGray,
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
                    tint = ProductSearchTextGray
                )
            }
        }
    }
}

@Composable
private fun PopularSearchItem(
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
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4F55)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = keyword,
            fontSize = 14.sp,
            color = Color(0xFF4A4F55)
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ProductSearchScreenPreview() {
    ProductSearchScreen(
        uiState = ProductSearchScreenUiState.Success(
            searchQuery = "",
            recentSearches = listOf(
                "디딤돌 대출",
                "주택청약종합저축",
                "국민은행"
            ),
            popularSearches = listOf(
                "디딤돌 대출",
                "버팀목 전세대출",
                "청년 주택드림 청약통장",
                "주택청약종합저축",
                "국민은행"
            )
        ),
        onBack = {},
        onSearchQueryChange = {},
        onDeleteRecentSearch = {},
        onSearch = {}
    )
}
