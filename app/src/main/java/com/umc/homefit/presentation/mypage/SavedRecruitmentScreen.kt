package com.umc.homefit.presentation.mypage

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.AutoDismissInfoSnackbar
import com.umc.homefit.presentation.component.ErrorStateView
import com.umc.homefit.presentation.component.NoticeCard
import com.umc.homefit.presentation.component.NoticeCardAction
import com.umc.homefit.presentation.component.NoticeCardUiModel
import androidx.compose.ui.text.style.TextAlign

private val MutedTextColor = Color(0xFF919AA4)
private val SelectedTextColor = Color(0xFF4A4F55)

@Composable
fun SavedRecruitmentScreenRoute(
    viewModel: SavedRecruitmentScreenViewModel,
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SavedRecruitmentScreen(
        uiState = uiState,
        onBack = onBack,
        onSortOptionSelected = viewModel::onSortOptionSelected,
        onRemoveClick = viewModel::onRemoveClick,
        onLoadMore = viewModel::loadNextPage,
        onNavigateToDetail = onNavigateToDetail,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

@Composable
fun SavedRecruitmentScreen(
    uiState: SavedRecruitmentScreenUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSortOptionSelected: (SortOption) -> Unit = {},
    onRemoveClick: (String) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    var showRemovedMessage by remember { mutableStateOf(false) }

    AppScaffold(
        title = "관심 공고 관리",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is SavedRecruitmentScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SavedRecruitmentScreenUiState.Success -> {
                    SavedRecruitmentContent(
                        items = uiState.items,
                        sortOption = uiState.sortOption,
                        isLoadingMore = uiState.isLoadingMore,
                        onSortOptionSelected = onSortOptionSelected,
                        onRemoveClick = { id ->
                            onRemoveClick(id)
                            showRemovedMessage = true
                        },
                        onLoadMore = onLoadMore,
                        onCardClick = onNavigateToDetail,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is SavedRecruitmentScreenUiState.Error -> {
                    ErrorStateView(message = uiState.message, onRetry = onRetry)
                }
            }

            AutoDismissInfoSnackbar(
                visible = showRemovedMessage,
                message = "관심 공고에서 삭제되었습니다",
                onDismiss = { showRemovedMessage = false }
            )
        }
    }
}

@Composable
private fun SavedRecruitmentContent(
    items: List<NoticeCardUiModel>,
    sortOption: SortOption,
    isLoadingMore: Boolean,
    onSortOptionSelected: (SortOption) -> Unit,
    onRemoveClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        EmptySavedRecruitmentView(modifier = modifier)
        return
    }

    val listState = rememberLazyListState()

    LaunchedEffect(listState, items.size) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= items.size - 3) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 43.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                SortDropdown(
                    selectedOption = sortOption,
                    onOptionSelected = onSortOptionSelected
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }

        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            NoticeCard(
                uiModel = item,
                onClick = { onCardClick(item.id) },
                action = NoticeCardAction.Remove(onRemove = { onRemoveClick(item.id) })
            )
            if (index != items.lastIndex) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun EmptySavedRecruitmentView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "아직 관심 공고가 없어요",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SelectedTextColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "마음에 드는 공고를 하트를 눌러 저장해보세요",
                fontSize = 13.sp,
                color = MutedTextColor
            )
        }
    }
}

@Composable
private fun SortDropdown(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // TODO: 백엔드가 LATEST만 지원해서 우선 최신순만 노출, 다른 옵션 지원되면 SortOption.entries로 되돌리기
    val availableOptions = listOf(SortOption.LATEST)

    Row(
        modifier = modifier.clickable { expanded = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedOption.label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MutedTextColor
        )
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = MutedTextColor,
            modifier = Modifier.size(18.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            availableOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = if (option == selectedOption) SelectedTextColor else MutedTextColor,
                            fontWeight = if (option == selectedOption) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedRecruitmentScreenPreview() {
    SavedRecruitmentScreen(
        uiState = SavedRecruitmentScreenUiState.Success(
            items = emptyList()
        ),
        onBack = {}
    )
}
