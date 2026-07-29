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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.presentation.component.AppScaffold

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
            .padding(top = 16.dp)
    ) {
        RecruitmentSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "최근 검색어",
            modifier = Modifier.padding(horizontal = 14.dp),
            color = Color(0xFF18191B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (recentSearches.isEmpty()) {
            Text(
                text = "최근 검색어가 없습니다.",
                modifier = Modifier.padding(horizontal = 14.dp),
                color = Color(0xFF919AA4),
                fontSize = 13.sp
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 14.dp),
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

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "인기 검색어",
            modifier = Modifier.padding(horizontal = 14.dp),
            color = Color(0xFF18191B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(22.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 14.dp,
                end = 14.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            itemsIndexed(
                items = popularSearches,
                key = { index, keyword ->
                    "$index-$keyword"
                }
            ) { index, keyword ->
                RecruitmentPopularSearchItem(
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
private fun RecruitmentSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = Color(0xFFF0F4F9),
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        bottomStart = 4.dp
                    )
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = Color(0xFF18191B),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                cursorBrush = SolidColor(
                    Color(0xFF3C45F3)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(searchQuery)
                    }
                ),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "공고명, 지하철역명, 단지명 등으로 검색",
                                color = Color(0xFF919AA4),
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = LocalTextStyle.current.copy(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }

                        innerTextField()
                    }
                }
            )
        }

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
                    ),
                    shape = RoundedCornerShape(
                        topEnd = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .clickable {
                    onSearch(searchQuery)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.size(25.dp)
            )
        }
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
        shape = RoundedCornerShape(50.dp),
        color = Color(0xFFFFFFFF),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFDCE2E9)
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
                color = Color(0xFF919AA4),
                fontSize = 14.sp
            )

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp)
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
            modifier = Modifier.width(16.dp),
            color = Color(0xFF4A4F55),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(4.dp))

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
