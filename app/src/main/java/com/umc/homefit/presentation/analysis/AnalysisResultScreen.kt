package com.umc.homefit.presentation.analysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umc.homefit.R
import com.umc.homefit.ui.component.AppTopBar
import com.umc.homefit.ui.component.TopBarAction

private val BorderColor = Color(0xFFD2D9E2)

@Composable
fun AnalysisResultScreenRoute(
    viewModel: AnalysisResultScreenViewModel,
    onBack: () -> Unit,
    onNavigateToEstimatedCost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalysisResultScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToEstimatedCost = onNavigateToEstimatedCost,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisResultScreen(
    uiState: AnalysisResultScreenUiState,
    onBack: () -> Unit,
    onNavigateToEstimatedCost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "입주 분석 결과",
                showBackButton = true,
                onBackClick = onBack,
                actions = listOf(
                    TopBarAction(
                        icon = painterResource(id = R.drawable.ic_top_save),
                        contentDescription = "찜",
                        onClick = { /* 찜 기능 */ }
                    ),
                    TopBarAction(
                        icon = painterResource(id = R.drawable.ic_top_share),
                        contentDescription = "공유",
                        onClick = { /* 공유 기능 */ }
                    )
                ),
                showDivider = true
            )
        },
        bottomBar = {
            if (uiState is AnalysisResultScreenUiState.Success) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF3C45F3)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3C45F3))
                    ) {
                        Text("홈으로", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onNavigateToEstimatedCost("cost_abc") },
                        modifier = Modifier
                            .weight(2f)
                            .height(52.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF3C45F3),
                                        Color(0xFF3C45F3).copy(alpha = 0.5f)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text("추천 금융상품 보기", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
        ) {
            when (uiState) {
                is AnalysisResultScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF3C45F3))
                }
                is AnalysisResultScreenUiState.Error -> {
                    Text(text = "Error: ${uiState.message}", modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
                is AnalysisResultScreenUiState.Success -> {
                    SuccessContent(data = uiState.data)
                }
            }
        }
    }
}

@Composable
private fun SuccessContent(data: AnalysisResultData) {
    var isAccordionExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 입주 가능성 카드
        Column {
            Text("입주 가능성", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BorderColor),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {

                    // 좌하단 장식
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_cloud_bottom),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )

                    // 우상단 장식
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_cloud_top),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )

                    // 큰 원
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_dot_large),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = (-24).dp, y = (-2).dp)
                    )

                    // 작은 원
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_dot_small),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(x = 100.dp, y = 32.dp)
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_dot_small),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = (-35).dp, y = 24.dp)
                            .size(7.dp) // 큰 원보다 더 작게 하고 싶으면 사이즈 지정, 필요 없으면 이 줄 삭제
                    )

                    // 왼쪽 텍스트
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 28.dp)
                    ) {

                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color(0xFFF3F2FF)
                        ) {
                            Text(
                                text = data.percentileText,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), // 18/8 → 16/6, 뱃지가 이미지보다 좀 두꺼워 보임
                                color = Color(0xFF3C45F3),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp)) // 16 → 10, 뱃지와 "높음" 사이 간격이 이미지에선 더 촘촘함

                        Text(
                            text = data.probabilityGrade,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF3C45F3)
                        )
                    }

                    // 집
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 20.dp)      // 28 → 20, 집이 이미지에서 카드 오른쪽 끝에 더 붙어있음
                            .offset(x = 16.dp, y = 8.dp) // 20 → 16
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_analysis_house),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(width = 220.dp, height = 138.dp) // 미세 축소
                        )

                        Text(
                            text = "${data.score}점",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = 6.dp), // 이미지 보면 점수 텍스트가 중앙보다 살짝 아래쪽
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 예상 비용 카드
        Column {
            Text("예상 비용", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("보증금", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(data.expectedDeposit, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF4A4F55))
                    }
                    VerticalDivider(modifier = Modifier.height(40.dp), color = BorderColor)
                    Column(modifier = Modifier.weight(1f).padding(start = 20.dp)) {
                        Text("월세", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(data.expectedMonthlyRent, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF4A4F55))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF0F4F9)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = data.infoTags.getOrElse(0) { "" },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF919AA4),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.height(16.dp),
                        thickness = 1.dp,
                        color = BorderColor
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = data.infoTags.getOrElse(1) { "" },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF919AA4),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 조건 충족 현황 리스트
        Column {
            Text("조건 충족 현황", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Column {
                    data.criteriaStatus.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1A1A1A))
                            Text(
                                text = item.statusText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (item.isSuitable) Color(0xFF299251) else Color(0xFFFFC300)
                            )
                        }
                        if (index != data.criteriaStatus.lastIndex) {
                            HorizontalDivider(color = BorderColor)
                        }
                    }
                }
            }
        }

        // 분석 기준 보기 아코디언 드롭다운
        Card(
            modifier = Modifier.fillMaxWidth().clickable { isAccordionExpanded = !isAccordionExpanded },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("분석 기준 보기", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Icon(
                    imageVector = if (isAccordionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF4A4F55)
                )
            }
            if (isAccordionExpanded) {
                Text(
                    text = "여기에 분석 세부 기준 내용이 들어갑니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }

        // 유의사항 영역
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "*유의사항",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFE53E3E).copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "위 결과는 입력하신 정보를 기반으로 산출한 예상 결과입니다.\n실제 심사 결과와 다를 수 있습니다.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFE53E3E).copy(alpha = 0.5f),
                    lineHeight = MaterialTheme.typography.labelSmall.lineHeight * 1.3f
                )
            }
        }
    }
}
