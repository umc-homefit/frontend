package com.umc.homefit.presentation.analysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items

@Composable
fun AnalysisScreenRoute(
    viewModel: AnalysisScreenViewModel,
    onNavigateToMyFinance: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalysisScreen(
        uiState = uiState,
        onNavigateToFinancialInfo = onNavigateToMyFinance,
        modifier = modifier
    )
}

@Composable
fun AnalysisScreen(
    uiState: AnalysisScreenUiState,
    onNavigateToFinancialInfo: () -> Unit,
    modifier: Modifier = Modifier,
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
                    AnalysisTab.FINANCIAL_INFO -> FinancialInfoEmptyContent(
                        onNavigateToFinancialInfo = onNavigateToFinancialInfo
                    )

                    AnalysisTab.RECORD -> RecordListContent(records = uiState.records)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Tab Row (분석 화면 하위 탭: 금융 정보 관리 / 기록)
// ---------------------------------------------------------------------------

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

// 금융 정보 관리 탭

@Composable
private fun FinancialInfoEmptyContent(
    onNavigateToFinancialInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // 금융 정보 관리 버튼
        Card(
            onClick = onNavigateToFinancialInfo,
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFD2D9EC)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_analysis_bank),
                    contentDescription = null,
                    tint = Color(0xFF2F6FED),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "금융 정보 관리",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A4F55),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_analysis_chevron_right),
                    contentDescription = null,
                    tint = Color(0xFF919AA4),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 캐릭터 일러스트
        Image(
            painter = painterResource(id = R.drawable.ic_analysis_empty),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(
                    x = (-26).dp,
                    y = (-52).dp
                )
        )
    }
}

// 기록 탭

@Composable
private fun RecordListContent(
    records: List<RecordItem>,
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
            RecordCard(record = record)
        }
    }
}

@Composable
private fun RecordCard(
    record: RecordItem,
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
                    Spacer(modifier = Modifier.width(5.dp))
                    CompetitionBadge(rate = record.competitionRate)
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, modifier: Modifier = Modifier) {
    val isRecruiting = label == "모집중"
    val bgColor = if (isRecruiting) Color(0xFFF1F0FF) else Color(0xFFF0F4F9)
    val textColor = if (isRecruiting) Color(0xFF3C45F3) else Color(0xFF4A4F55)

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

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
private fun AnalysisScreenFinancialInfoEmptyPreview() {
    AnalysisScreen(
        uiState = AnalysisScreenUiState.Success(),
        onNavigateToFinancialInfo = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AnalysisScreenRecordListPreview() {
    val sample = RecordItem(
        date = "2026.07.07",
        title = "강동구 청년안심주택 2025-03호",
        complexInfo = "공고번호 : 2024-강동-031",
        areaInfo = "전용 : 59㎡ 보증금 : 3,200만원",
        applyPeriod = "청약접수 : 2026.07.05 ~ 2026.07.08",
        statusLabel = "모집중",
        competitionRate = "12:1"
    )
    AnalysisScreen(
        uiState = AnalysisScreenUiState.Success(
            records = listOf(
                sample,
                sample.copy(date = "2026.07.01", title = "강동구 고덕강일 청년안심주택", statusLabel = "예정"),
                sample.copy(date = "2026.06.28", title = "강동구 고덕강일 청년안심주택", statusLabel = "예정"),
                sample.copy(date = "2026.06.25", title = "강동구 고덕강일 청년안심주택", statusLabel = "예정")
            )
        ),
        onNavigateToFinancialInfo = {}
    )
}
