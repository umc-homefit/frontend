package com.umc.homefit.presentation.finance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.homefit.presentation.finance.component.RecommendedProductSearchBar
import com.umc.homefit.ui.component.AppScaffold
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
            .padding(top = 16.dp)
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

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "최근 검색어",
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF24282D)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (recentSearches.isEmpty()) {
            Text(
                text = "최근 검색어가 없습니다.",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = ProductSearchTextGray
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
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
                        },
                        onDeleteClick = {
                            onDeleteRecentSearch(keyword)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "인기 검색어",
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF24282D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(
                items = popularSearches,
                key = { index, keyword ->
                    "$index-$keyword"
                }
            ) { index, keyword ->
                PopularSearchItem(
                    rank = index + 1,
                    keyword = keyword,
                    onClick = {
                        onSearchQueryChange(keyword)
                        onSearch(keyword)
                    }
                )
            }
        }
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
                start = 14.dp,
                end = 8.dp,
                top = 6.dp,
                bottom = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = keyword,
                color = ProductSearchTextGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp)
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
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4F55)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium,
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
