package com.umc.homefit.presentation.mypage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.ui.component.AppScaffold
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clip

private val CardBorderColor = Color(0xFFD2D9E2)
private val GrayChipColor = Color(0xFFF0F4F9)
private val GrayChipTextColor = Color(0xFF6B7280)
private val RecruitingChipColor = Color(0xFFE3F2FD)
private val RecruitingTextColor = Color(0xFF1E88E5)

@Composable
fun SavedRecruitmentScreenRoute(
    viewModel: SavedRecruitmentScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    SavedRecruitmentScreen(
        uiState = uiState,
        onBack = onBack,
        onSortOptionSelected = viewModel::onSortOptionSelected,
        onRemoveClick = viewModel::onRemoveClick,
        modifier = modifier
    )
}

@Composable
fun SavedRecruitmentScreen(
    uiState: SavedRecruitmentScreenUiState,
    onBack: () -> Unit,
    onSortOptionSelected: (SortOption) -> Unit = {},
    onRemoveClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showRemovedMessage by remember { mutableStateOf(false) }

    LaunchedEffect(showRemovedMessage) {
        if (showRemovedMessage) {
            delay(2000)
            showRemovedMessage = false
        }
    }

    AppScaffold(
        title = "관심 공고 관리",
        showBackButton = true,
        onBackClick = onBack,
        modifier = modifier
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (uiState) {
                is SavedRecruitmentScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SavedRecruitmentScreenUiState.Success -> {
                    SavedRecruitmentContent(
                        items = uiState.items,
                        sortOption = uiState.sortOption,
                        onSortOptionSelected = onSortOptionSelected,
                        onRemoveClick = { id ->
                            onRemoveClick(id)
                            showRemovedMessage = true
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is SavedRecruitmentScreenUiState.Error -> {
                    Text(text = "Error: ${uiState.message}", modifier = Modifier.align(Alignment.Center))
                }
            }

            AnimatedVisibility(
                visible = showRemovedMessage,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                RemovedSnackbar(message = "관심 공고에서 삭제되었습니다")
            }
        }
    }
}

@Composable
private fun RemovedSnackbar(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4A4F55), RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = message, fontSize = 14.sp, color = Color.White)
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = Color(0xFF34A853)
        )
    }
}

@Composable
private fun SavedRecruitmentContent(
    items: List<SavedRecruitmentItem>,
    sortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
        }

        items(items, key = { it.id }) { item ->
            SavedRecruitmentCard(
                item = item,
                onRemoveClick = { onRemoveClick(item.id) }
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

    Row(
        modifier = modifier.clickable { expanded = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedOption.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            SortOption.values().forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
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

@Composable
private fun SavedRecruitmentCard(
    item: SavedRecruitmentItem,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "관심 공고 해제",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "공고번호 | ${item.noticeNumber}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "전용 | ${item.exclusiveArea}   보증금 | ${item.deposit}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "청약접수 | ${item.applicationPeriod}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusChip(status = item.status)

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .height(24.dp)
                    .clip(RoundedCornerShape(50))
                    .background(GrayChipColor)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83D\uDD25경쟁률 ${item.competitionRate}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = GrayChipTextColor
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: RecruitmentStatus) {
    val backgroundColor = when (status) {
        RecruitmentStatus.SCHEDULED -> GrayChipColor
        RecruitmentStatus.RECRUITING -> RecruitingChipColor
    }
    val textColor = when (status) {
        RecruitmentStatus.SCHEDULED -> GrayChipTextColor
        RecruitmentStatus.RECRUITING -> RecruitingTextColor
    }

    Box(
        modifier = Modifier
            .height(24.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedRecruitmentScreenPreview() {
    SavedRecruitmentScreen(
        uiState = SavedRecruitmentScreenUiState.Success(
            items = listOf(
                SavedRecruitmentItem(
                    id = "1",
                    title = "강동구 고덕강일 청년안심주택",
                    noticeNumber = "2024-강동-031",
                    exclusiveArea = "59㎡",
                    deposit = "3,200만원",
                    applicationPeriod = "2026.07.05 ~ 2026.07.08",
                    status = RecruitmentStatus.SCHEDULED,
                    competitionRate = "12:1"
                ),
                SavedRecruitmentItem(
                    id = "2",
                    title = "강동구 청년안심주택 2025-03호",
                    noticeNumber = "2024-강동-031",
                    exclusiveArea = "59㎡",
                    deposit = "3,200만원",
                    applicationPeriod = "2026.07.05 ~ 2026.07.08",
                    status = RecruitmentStatus.RECRUITING,
                    competitionRate = "12:1"
                )
            )
        ),
        onBack = {}
    )
}
