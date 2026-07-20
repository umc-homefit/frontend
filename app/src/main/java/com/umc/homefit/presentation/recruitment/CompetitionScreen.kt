package com.umc.homefit.presentation.recruitment

import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.data.dto.Attachment
import com.umc.homefit.data.dto.CompetitionDto
import com.umc.homefit.data.dto.CompetitionHistoryEntry
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
import com.umc.homefit.data.dto.TypeCompetitionRate
import com.umc.homefit.presentation.recruitment.component.RecruitmentTabRow
import com.umc.homefit.presentation.recruitment.component.RecruitmentTitleCard
import com.umc.homefit.presentation.recruitment.component.RecruitmentTopBar
import com.umc.homefit.ui.theme.AnalysisButtonGradient
import com.umc.homefit.ui.theme.BackgroundLight
import com.umc.homefit.ui.theme.RecruitmentAccent
import com.umc.homefit.ui.theme.RecruitmentBorder
import com.umc.homefit.ui.theme.RecruitmentTextGray
import com.umc.homefit.ui.theme.SearchFieldBackground
import com.umc.homefit.ui.theme.StatusClosingSoonBackground
import com.umc.homefit.ui.theme.StatusClosingSoonText
import com.umc.homefit.ui.theme.StatusScheduledText
import com.umc.homefit.ui.theme.TextBlack

// TODO: 디자이너 확인 후 theme 토큰으로 교체
private val CompetitionIntroGradientStart = Color(0xFF3D81FF)
private val CompetitionIntroGradientEnd = Color(0xFFCEEBFF)
private val CompetitionScrollDotColor = Color(0xFFC3E3FF)

@Composable
fun CompetitionScreenRoute(
    viewModel: CompetitionScreenViewModel,
    onBack: () -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    CompetitionScreen(
        uiState = uiState,
        onBack = onBack,
        onToggleBookmark = viewModel::toggleBookmark,
        onNavigateToAnalysis = onNavigateToAnalysis,
        modifier = modifier
    )
}

@Composable
fun CompetitionScreen(
    uiState: CompetitionScreenUiState,
    onBack: () -> Unit,
    onToggleBookmark: () -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
    ) {
        RecruitmentTopBar(
            isBookmarked = (uiState as? CompetitionScreenUiState.Success)?.recruitment?.isBookmarked == true,
            onBackClick = onBack,
            onBookmarkClick = onToggleBookmark
        )
        when (uiState) {
            is CompetitionScreenUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CompetitionScreenUiState.Success -> {
                CompetitionContent(
                    recruitment = uiState.recruitment,
                    competition = uiState.competition,
                    onNavigateBackToDetail = onBack,
                    onNavigateToAnalysis = onNavigateToAnalysis
                )
            }
            is CompetitionScreenUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${uiState.message}")
                }
            }
        }
    }
}

@Composable
private fun CompetitionContent(
    recruitment: RecruitmentDto,
    competition: CompetitionDto,
    onNavigateBackToDetail: () -> Unit,
    onNavigateToAnalysis: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RecruitmentTitleCard(recruitment)
        RecruitmentTabRow(
            selectedTabIndex = 1,
            onTabClick = { index -> if (index == 0) onNavigateBackToDetail() }
        )

        CompetitionIntroSection()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
                .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            CompetitionScrollIndicator()
            FinalRateHeroSection(finalRate = competition.finalRate, baseDate = competition.finalRateBaseDate)
            ExpectedScoreCard(score = competition.expectedScore, label = competition.expectedScoreLabel)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(101.5.dp))
                SectionTitle("세부 지표")
                Spacer(modifier = Modifier.height(12.dp))
                CompetitionDetailStatsTable(competition = competition)

                Spacer(modifier = Modifier.height(36.dp))
                SectionTitle("유형별 경쟁률")
                Spacer(modifier = Modifier.height(12.dp))
                TypeCompetitionTable(typeRates = competition.typeRates)

                Spacer(modifier = Modifier.height(36.dp))
                SectionTitle("최근 경쟁률 추이")
                Spacer(modifier = Modifier.height(12.dp))
                CompetitionTrendChart(history = competition.history)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "( 동일단지 또는 인근 지역 분양 기준 )",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RecruitmentBorder
                )

                Spacer(modifier = Modifier.height(16.dp))
                CompetitionHistoryTable(history = competition.history)

                Spacer(modifier = Modifier.height(16.dp))
                CompetitionNoticeBox()

                Spacer(modifier = Modifier.height(16.dp))
                AnalysisRequestButton(onClick = { onNavigateToAnalysis(competition.noticeId) })
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CompetitionIntroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(231.dp)
            .background(Brush.verticalGradient(listOf(CompetitionIntroGradientStart, CompetitionIntroGradientEnd)))
    ) {
        Text(
            text = "과거 공고 데이터를 기준으로\n경쟁률을 제공해요!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.offset(x = 24.dp, y = 32.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_competition_cloud_l),
            contentDescription = null,
            modifier = Modifier
                .offset(x = (-24).dp, y = 126.dp)
                .size(width = 69.dp, height = 34.81.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_competition_cloud_r),
            contentDescription = null,
            modifier = Modifier
                .offset(x = 264.dp, y = 59.dp)
                .size(width = 111.dp, height = 56.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_competition_character),
            contentDescription = null,
            modifier = Modifier
                .offset(x = 44.dp, y = 122.dp)
                .size(width = 110.dp, height = 101.7.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_competition_ants),
            contentDescription = null,
            modifier = Modifier
                .offset(x = 172.dp, y = 194.84.dp)
                .size(width = 130.dp, height = 15.81.dp)
        )
    }
}

@Composable
private fun CompetitionScrollIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 103.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(44.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(CompetitionScrollDotColor)
            )
        }
    }
}

@Composable
private fun FinalRateHeroSection(finalRate: String, baseDate: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 103.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "최종 경쟁률", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack, textAlign = TextAlign.Center)
        Text(text = baseDate, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(21.dp))
        Text(text = finalRate.replace(":", " : "), fontSize = 59.17.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        Spacer(modifier = Modifier.height(67.dp))
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(88.dp)
                .background(SearchFieldBackground)
        )
    }
}

@Composable
private fun ExpectedScoreCard(score: Int, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 67.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "당첨 가능 점수", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack, textAlign = TextAlign.Center)
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier.size(width = 189.dp, height = 196.875.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_competition_score_house),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "${score}점",
                fontSize = 59.17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 84.38.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
}

@Composable
private fun CompetitionDetailStatsTable(competition: CompetitionDto) {
    val rows = listOf(
        "총 공급 세대수" to "${competition.totalUnits}세대",
        "총 신청 건수" to "${competition.totalApplicants}건",
        "1순위 경쟁률" to competition.firstPriorityRate,
        "2순위 경쟁률" to competition.secondPriorityRate,
        "특별공급 경쟁률" to competition.specialSupplyRate,
        "일반공급 경쟁률" to competition.generalSupplyRate,
        "신청 기간" to competition.applicationPeriod
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
            .padding(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        rows.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = StatusScheduledText
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RecruitmentTextGray,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun TypeCompetitionTable(typeRates: List<TypeCompetitionRate>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
    ) {
        CompetitionTableRow(cells = listOf("주택형", "공급 세대", "신청 건수", "경쟁률"), isHeader = true)
        typeRates.forEach { type ->
            HorizontalDivider(color = SearchFieldBackground, thickness = 1.dp)
            CompetitionTableRow(
                cells = listOf(type.unitType, "${type.supplyUnits}세대", "${type.applicantCount}건", type.rate),
                isHeader = false
            )
        }
    }
}

@Composable
private fun CompetitionTableRow(cells: List<String>, isHeader: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isHeader) Modifier.background(SearchFieldBackground) else Modifier)
    ) {
        cells.forEach { text ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isHeader) StatusScheduledText else RecruitmentTextGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CompetitionTrendChart(history: List<CompetitionHistoryEntry>) {
    if (history.isEmpty()) return

    // TODO: 지금은 60 고정값 사용, 추후 history.maxOf { it.rate } 기준으로 동적 계산하도록 개선
    val maxRate = 60f
    val yAxisValues = listOf(60, 50, 40, 30, 20, 10, 0)

    val gridColor = RecruitmentBorder
    val lineColor = RecruitmentAccent
    // Figma 0x0D3C45F3 = RecruitmentAccent, alpha 0x0D/255 (약 5%)
    val fillColor = RecruitmentAccent.copy(alpha = 13f / 255f)
    val axisLabelColor = RecruitmentTextGray.toArgb()
    val pointLabelColor = RecruitmentAccent.toArgb()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(216.dp)
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(174.dp)
                .padding(start = 14.dp, top = 21.dp, end = 14.dp)
        ) {
            val leftPadding = 28.dp.toPx()
            val topPadding = 10.dp.toPx()
            val bottomPadding = 14.dp.toPx()
            val chartWidth = size.width - leftPadding
            val chartHeight = size.height - topPadding - bottomPadding
            val chartBottomY = topPadding + chartHeight

            val yAxisTextPaint = Paint().apply {
                color = axisLabelColor
                textSize = 10.sp.toPx()
                textAlign = Paint.Align.RIGHT
                isAntiAlias = true
            }
            val xAxisTextPaint = Paint().apply {
                color = axisLabelColor
                textSize = 10.sp.toPx()
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            val pointLabelPaint = Paint().apply {
                color = pointLabelColor
                textSize = 10.sp.toPx()
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
                isAntiAlias = true
            }

            yAxisValues.forEach { value ->
                val y = topPadding + chartHeight * (1f - value / maxRate)
                drawLine(
                    color = gridColor,
                    start = Offset(leftPadding, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
                val label = if (value == 0) "0" else "$value:1"
                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    leftPadding - 6.dp.toPx(),
                    y + 3.dp.toPx(),
                    yAxisTextPaint
                )
            }

            val stepX = if (history.size > 1) chartWidth / (history.size - 1) else 0f
            val points = history.mapIndexed { index, entry ->
                val x = leftPadding + stepX * index
                val ratio = (entry.rate / maxRate).coerceIn(0.0, 1.0).toFloat()
                val y = topPadding + chartHeight * (1f - ratio)
                Offset(x, y)
            }

            val linePath = Path().apply {
                points.forEachIndexed { index, offset ->
                    if (index == 0) moveTo(offset.x, offset.y) else lineTo(offset.x, offset.y)
                }
            }

            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(points.last().x, chartBottomY)
                lineTo(points.first().x, chartBottomY)
                close()
            }
            drawPath(path = fillPath, color = fillColor)
            drawPath(path = linePath, color = lineColor, style = Stroke(width = 2.dp.toPx()))

            points.forEachIndexed { index, offset ->
                drawCircle(color = lineColor, radius = 4.dp.toPx(), center = offset)
                drawContext.canvas.nativeCanvas.drawText(
                    "${history[index].rate}:1",
                    offset.x,
                    offset.y - 10.dp.toPx(),
                    pointLabelPaint
                )
                drawContext.canvas.nativeCanvas.drawText(
                    history[index].roundLabel,
                    offset.x,
                    chartBottomY + 10.dp.toPx(),
                    xAxisTextPaint
                )
            }
        }
    }
}

@Composable
private fun CompetitionHistoryTable(history: List<CompetitionHistoryEntry>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
    ) {
        CompetitionHistoryTableRow(cells = listOf("분양 회차", "공급 세대", "신청 건수", "경쟁률"), isHeader = true, isLatest = false)
        history.forEachIndexed { index, entry ->
            HorizontalDivider(color = SearchFieldBackground, thickness = 1.dp)
            CompetitionHistoryTableRow(
                cells = listOf(entry.roundLabel, "${entry.supplyUnits}세대", "${entry.applicantCount}건", "${entry.rate}:1"),
                isHeader = false,
                isLatest = index == history.lastIndex
            )
        }
    }
}

@Composable
private fun CompetitionHistoryTableRow(cells: List<String>, isHeader: Boolean, isLatest: Boolean) {
    val textColor = when {
        isLatest -> RecruitmentAccent
        isHeader -> StatusScheduledText
        else -> RecruitmentTextGray
    }
    val fontWeight = if (isLatest) FontWeight.Bold else FontWeight.Medium

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isHeader) Modifier.background(SearchFieldBackground) else Modifier)
    ) {
        cells.forEach { text ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = fontWeight,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CompetitionNoticeBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(StatusClosingSoonBackground, RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "*경쟁률 산정 및 유의사항",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = StatusClosingSoonText.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = "본 경쟁률은 청약홈 공식 발표 기준이며,\n최종 당첨 결과와 다를 수 있습니다.\n\n가점 산정 및 순위 조건은 공고문을 반드시 확인하세요.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = StatusClosingSoonText.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun AnalysisRequestButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(AnalysisButtonGradient, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "입주 분석 요청하기", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun CompetitionScreenPreview() {
    CompetitionScreen(
        uiState = CompetitionScreenUiState.Success(
            recruitment = RecruitmentDto(
                id = "1",
                title = "강동구 청년안심주택 2025-03호",
                company = "한국토지주택공사",
                location = "서울 강동구 천호동 123-4",
                rentType = "청년안심주택 (임대)",
                unitSummary = "전용 24㎡ 18세대 / 전용 33㎡ 12세대",
                depositMin = 32000000,
                depositMax = 48000000,
                monthlyRentMin = 280000,
                monthlyRentMax = 410000,
                announcementDate = "2026-07-13",
                announcementNumber = "2026-강남-001",
                area = 39.87,
                applicationStartDate = "2025년 6월 9일 (월) 오전 10:00",
                applicationEndDate = "2025년 6월 13일 (금) 오후 6:00",
                status = RecruitmentStatus.RECRUITING,
                competitionRate = "12.3:1",
                isBookmarked = true,
                tags = listOf("청년우선공급", "역세권"),
                attachments = listOf(
                    Attachment(fileName = "2025-03호 공고문 (PDF)", registeredDate = "2025.06.02 등록"),
                    Attachment(fileName = "입주자 모집 안내 책자", registeredDate = "2025.06.02 등록"),
                    Attachment(fileName = "서울주택도시공사 청약 신청 매뉴얼", registeredDate = "2025.05.28 등록")
                ),
                moveInDate = "2025년 9월",
                ageRange = "만 19세 ~ 39세",
                incomeStandard = "도시근로자 월평균 소득 100% 이하",
                assetStandard = "총 자산 3억 6,100만 원 이하",
                housingOwnership = "무주택 세대구성원",
                residencyRequirement = "서울시 거주 또는 직장 소재",
                winnerAnnouncementDate = "2025년 7월 4일 (금)",
                contractPeriod = "2025년 7월 14일 ~ 7월 18일",
                photoResIds = listOf(
                    R.drawable.img_recruitment_1,
                    R.drawable.img_recruitment_2,
                    R.drawable.img_recruitment_3,
                    R.drawable.img_recruitment_4
                )
            ),
            competition = CompetitionDto(
                noticeId = "1",
                finalRate = "47.3:1",
                finalRateBaseDate = "2025.06.02 기준 최종 집계",
                expectedScore = 60,
                expectedScoreLabel = "60점 이상",
                totalUnits = 120,
                totalApplicants = 5676,
                firstPriorityRate = "38.2:1",
                secondPriorityRate = "9.1:1",
                specialSupplyRate = "22.7:1",
                generalSupplyRate = "61.4:1",
                applicationPeriod = "2025.05.28 ~ 2025.06.01",
                typeRates = listOf(
                    TypeCompetitionRate(unitType = "16A", supplyUnits = 20, applicantCount = 964, rate = "48.2:1"),
                    TypeCompetitionRate(unitType = "24A", supplyUnits = 32, applicantCount = 1856, rate = "58.0:1"),
                    TypeCompetitionRate(unitType = "36A", supplyUnits = 26, applicantCount = 1016, rate = "39.1:1"),
                    TypeCompetitionRate(unitType = "39A", supplyUnits = 18, applicantCount = 612, rate = "34.0:1")
                ),
                history = listOf(
                    CompetitionHistoryEntry(roundLabel = "23년 1차", supplyUnits = 110, applicantCount = 3124, rate = 28.4),
                    CompetitionHistoryEntry(roundLabel = "23년 2차", supplyUnits = 109, applicantCount = 3568, rate = 32.7),
                    CompetitionHistoryEntry(roundLabel = "24년 1차", supplyUnits = 113, applicantCount = 4689, rate = 41.5),
                    CompetitionHistoryEntry(roundLabel = "24년 2차", supplyUnits = 114, applicantCount = 5289, rate = 46.2),
                    CompetitionHistoryEntry(roundLabel = "25년 1차", supplyUnits = 120, applicantCount = 5676, rate = 47.3)
                )
            )
        ),
        onBack = {},
        onToggleBookmark = {},
        onNavigateToAnalysis = {}
    )
}
