package com.umc.homefit.presentation.analysis

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items

@Composable
fun AnalysisScreenRoute(
    viewModel: AnalysisScreenViewModel,
    onNavigateToEdit: (FinancialInfoStep) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalysisScreen(
        uiState = uiState,
        onNavigateToEdit = onNavigateToEdit,
        onRecordClick = onNavigateToDetail,
        modifier = modifier
    )
}

@Composable
fun AnalysisScreen(
    uiState: AnalysisScreenUiState,
    onNavigateToEdit: (FinancialInfoStep) -> Unit,
    onRecordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(AnalysisTab.FINANCIAL_INFO) }

    Column(modifier = modifier.fillMaxSize()) {
        AnalysisTabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        when (uiState) {
            is AnalysisScreenUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is AnalysisScreenUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${uiState.message}")
                }
            }

            is AnalysisScreenUiState.Success -> {
                when (selectedTab) {
                    AnalysisTab.FINANCIAL_INFO -> FinancialInfoContent(
                        sections = uiState.sections,
                        onEditClick = onNavigateToEdit
                    )

                    AnalysisTab.RECORD -> RecordListContent(
                        records = uiState.records,
                        onRecordClick = onRecordClick
                    )
                }
            }
        }
    }
}

// Tab Row

@Composable
private fun AnalysisTabRow(
    selectedTab: AnalysisTab,
    onTabSelected: (AnalysisTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        AnalysisTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFF161616) else Color(0xFFD2D9E2)
                    )
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = if (isSelected) Color(0xFF3C45F3) else Color(0xFFD2D9E2)
                )
            }
        }
    }
}

// 기록 탭

@Composable
private fun RecordListContent(
    records: List<RecordItem>,
    onRecordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 기록 없는 경우
    if (records.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "기록이 없습니다", color = Color(0xFF919AA4), fontSize = 14.sp)
        }
        return
    }

    // 기록 있는 경우
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 37.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(records) { record ->
            RecordCard(
                record = record,
                onClick = { onRecordClick(record.noticeId) }
            )
        }
    }
}

@Composable
private fun RecordCard(
    record: RecordItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = record.date,
            fontSize = 12.sp,
            color = Color(0xFF919AA4)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Card(
            onClick = onClick,
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFD2D9E2)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp)) {
                Text(
                    text = record.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF161616)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = record.complexInfo, fontSize = 12.sp, color = Color(0xFF919AA4))
                Text(text = record.areaInfo, fontSize = 12.sp, color = Color(0xFF919AA4))
                Text(text = record.applyPeriod, fontSize = 12.sp, color = Color(0xFF919AA4))
                Spacer(modifier = Modifier.height(9.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(label = record.statusLabel)
                    if (!record.competitionRate.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(5.dp))
                        CompetitionBadge(rate = record.competitionRate)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, modifier: Modifier = Modifier) {
    val isRecruiting = label == "모집중"
    val isClosingSoon = label == "마감임박"
    val bgColor = when {
        isRecruiting -> Color(0xFFF1F0FF)
        isClosingSoon -> Color(0xFFFFF6F6)
        else -> Color(0xFFF0F4F9)
    }
    val textColor = when {
        isRecruiting -> Color(0xFF3C45F3)
        isClosingSoon -> Color(0xFFFF5659)
        else -> Color(0xFF4A4F55)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(200.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CompetitionBadge(rate: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(200.dp))
            .background(Color(0xFFF0F4F9))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = "🔥경쟁률 $rate",
            fontSize = 12.sp,
            color = Color(0xFF4A4F55),
            fontWeight = FontWeight.Medium
        )
    }
}
