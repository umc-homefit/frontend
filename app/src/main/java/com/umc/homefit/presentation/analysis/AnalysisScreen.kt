package com.umc.homefit.presentation.analysis

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import com.umc.homefit.presentation.component.NoticeCard

@Composable
fun AnalysisScreenRoute(
    viewModel: AnalysisScreenViewModel,
    onNavigateToEdit: (FinancialInfoStep) -> Unit,
    onNavigateToDetail: (noticeId: String, analysisId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadConditionProfileSections()
    }

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
    onRecordClick: (noticeId: String, analysisId: String) -> Unit,
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
    records: List<RecordListItem>,
    onRecordClick: (noticeId: String, analysisId: String) -> Unit,
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
    val groupedRecords = records.groupBy { it.date }.toList()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 37.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(groupedRecords, key = { (date, _) -> date }) { (date, recordsForDate) ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color(0xFF919AA4)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recordsForDate.forEach { record ->
                        NoticeCard(
                            uiModel = record.card,
                            onClick = { onRecordClick(record.noticeId, record.analysisId) }
                        )
                    }
                }
            }
        }
    }
}

