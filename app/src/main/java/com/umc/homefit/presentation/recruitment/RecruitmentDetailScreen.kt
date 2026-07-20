package com.umc.homefit.presentation.recruitment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umc.homefit.R
import com.umc.homefit.data.dto.Attachment
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
import com.umc.homefit.presentation.recruitment.component.RecruitmentTabRow
import com.umc.homefit.presentation.recruitment.component.RecruitmentTitleCard
import com.umc.homefit.presentation.recruitment.component.RecruitmentTopBar
import com.umc.homefit.presentation.recruitment.component.StatusChip
import com.umc.homefit.presentation.recruitment.component.TagChip
import com.umc.homefit.ui.theme.AnalysisButtonGradient
import com.umc.homefit.ui.theme.BackgroundLight
import com.umc.homefit.ui.theme.RecruitmentAccent
import com.umc.homefit.ui.theme.RecruitmentBorder
import com.umc.homefit.ui.theme.RecruitmentTextGray
import com.umc.homefit.ui.theme.SearchFieldBackground
import com.umc.homefit.ui.theme.StatusClosingSoonText
import com.umc.homefit.ui.theme.StatusScheduledText
import com.umc.homefit.ui.theme.TextBlack
import java.util.Locale

@Composable
fun RecruitmentDetailScreenRoute(
    viewModel: RecruitmentDetailScreenViewModel,
    onBack: () -> Unit,
    onNavigateToCompetition: (String) -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    RecruitmentDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToCompetition = onNavigateToCompetition,
        onNavigateToAnalysis = onNavigateToAnalysis,
        onToggleBookmark = viewModel::toggleBookmark,
        modifier = modifier
    )
}

@Composable
fun RecruitmentDetailScreen(
    uiState: RecruitmentDetailScreenUiState,
    onBack: () -> Unit,
    onNavigateToCompetition: (String) -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    onToggleBookmark: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
    ) {
        RecruitmentTopBar(
            isBookmarked = (uiState as? RecruitmentDetailScreenUiState.Success)?.recruitment?.isBookmarked == true,
            onBackClick = onBack,
            onBookmarkClick = onToggleBookmark
        )
        when (uiState) {
            is RecruitmentDetailScreenUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is RecruitmentDetailScreenUiState.Success -> {
                RecruitmentDetailContent(
                    recruitment = uiState.recruitment,
                    onNavigateToCompetition = onNavigateToCompetition,
                    onNavigateToAnalysis = onNavigateToAnalysis
                )
            }
            is RecruitmentDetailScreenUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${uiState.message}")
                }
            }
        }
    }
}

@Composable
private fun RecruitmentDetailContent(
    recruitment: RecruitmentDto,
    onNavigateToCompetition: (String) -> Unit,
    onNavigateToAnalysis: (String) -> Unit
) {
    var showFullScreenViewer by remember { mutableStateOf(false) }
    var selectedPhotoIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            RecruitmentTitleCard(recruitment)
            RecruitmentTabRow(
                selectedTabIndex = 0,
                onTabClick = { index -> if (index == 1) onNavigateToCompetition(recruitment.id) }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoCard(rows = supplyInfoRows(recruitment))
                PhotoGrid(
                    recruitment = recruitment,
                    onPhotoClick = { index ->
                        selectedPhotoIndex = index
                        showFullScreenViewer = true
                    }
                )

                Column {
                    SectionTitle("자격 조건")
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoCard(rows = qualificationRows(recruitment))
                }

                Column {
                    SectionTitle("신청 기간")
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoCard(rows = applicationPeriodRows(recruitment))
                }

                SectionTitle("첨부 파일 및 안내 자료")
                recruitment.attachments.forEach { attachment ->
                    AttachmentItem(
                        fileName = attachment.fileName,
                        registeredDate = attachment.registeredDate
                    )
                }

                Text(
                    text = "*경쟁률 정보를 함께 확인하면 청약 전략 수립에 도움이 됩니다.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = StatusClosingSoonText.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        BottomButtonBar(
            onCompetitionClick = { onNavigateToCompetition(recruitment.id) },
            // TODO(미해결): 유닛 여러 개일 때 선택 UI 위치는 리비 팀과 아직 미조율
            onAnalysisClick = { onNavigateToAnalysis(recruitment.id) }
        )
    }

    if (showFullScreenViewer) {
        FullScreenPhotoViewer(
            photoResIds = recruitment.photoResIds,
            initialPage = selectedPhotoIndex,
            onDismiss = { showFullScreenViewer = false }
        )
    }
}

@Composable
private fun InfoCard(rows: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
            .padding(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        rows.forEach { (label, value) ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)) {
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

private fun supplyInfoRows(recruitment: RecruitmentDto) = listOf(
    "공급 위치" to recruitment.location,
    "공급 유형" to recruitment.rentType,
    "공급 세대수" to recruitment.unitSummary,
    "임대 보증금" to formatWonRange(recruitment.depositMin, recruitment.depositMax),
    "월 임대료" to formatWonRange(recruitment.monthlyRentMin, recruitment.monthlyRentMax),
    "입주 예정일" to recruitment.moveInDate
)

private fun qualificationRows(recruitment: RecruitmentDto) = listOf(
    "연령" to recruitment.ageRange,
    "소득 기준" to recruitment.incomeStandard,
    "자산 기준" to recruitment.assetStandard,
    "주택 소유" to recruitment.housingOwnership,
    "거주지 요건" to recruitment.residencyRequirement
)

private fun applicationPeriodRows(recruitment: RecruitmentDto) = listOf(
    "접수 시작" to recruitment.applicationStartDate,
    "접수 마감" to recruitment.applicationEndDate,
    "당첨자 발표" to recruitment.winnerAnnouncementDate,
    "계약 체결" to recruitment.contractPeriod
)

@Composable
private fun SectionTitle(title: String) {
    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
}

@Composable
private fun PhotoGrid(recruitment: RecruitmentDto, onPhotoClick: (Int) -> Unit) {
    val photoResIds = recruitment.photoResIds.takeIf { it.size >= 4 } ?: emptyList()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()) {
                PhotoCell(resId = photoResIds.getOrNull(0), onClick = { onPhotoClick(0) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
                PhotoCell(resId = photoResIds.getOrNull(1), onClick = { onPhotoClick(1) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
            }
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()) {
                PhotoCell(resId = photoResIds.getOrNull(2), onClick = { onPhotoClick(2) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
                PhotoCell(resId = photoResIds.getOrNull(3), onClick = { onPhotoClick(3) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_recruitment_lucide_zoom_in),
            contentDescription = "사진 확대",
            tint = TextBlack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp)
                .size(20.dp)
                .clickable(onClick = { onPhotoClick(0) })
        )
    }
}

@Composable
private fun PhotoCell(resId: Int?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    if (resId != null) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.clickable(onClick = onClick)
        )
    } else {
        Box(modifier = modifier.background(SearchFieldBackground))
    }
}

@Composable
private fun FullScreenPhotoViewer(
    photoResIds: List<Int>,
    initialPage: Int,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
        ) {
            val pagerState = rememberPagerState(initialPage = initialPage) { photoResIds.size }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableImage(resId = photoResIds[page])
            }

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "닫기",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(28.dp)
                    .clickable(onClick = onDismiss)
            )
        }
    }
}

@Composable
private fun ZoomableImage(resId: Int) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)
                    offset += pan
                }
            }
    )
}

@Composable
private fun AttachmentItem(fileName: String, registeredDate: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(BackgroundLight, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_recruitment_pdf),
            contentDescription = "PDF",
            tint = Color(0xFFEF5350),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 14.dp, y = 21.dp)
                .size(24.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 52.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = fileName, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray)
            Text(text = registeredDate, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = RecruitmentBorder)
        }
    }
}

@Composable
private fun BottomButtonBar(
    onCompetitionClick: () -> Unit,
    onAnalysisClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(118f)
                .height(48.dp)
                .background(BackgroundLight, RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, RecruitmentAccent), RoundedCornerShape(4.dp))
                .clickable(onClick = onCompetitionClick),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "경쟁률", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RecruitmentAccent)
        }

        Box(
            modifier = Modifier
                .weight(202f)
                .height(48.dp)
                .background(AnalysisButtonGradient, RoundedCornerShape(4.dp))
                .clickable(onClick = onAnalysisClick),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "입주 분석 요청하기", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

private fun formatWonRange(minWon: Long, maxWon: Long): String {
    val min = String.format(Locale.KOREA, "%,d", minWon / 10_000)
    val max = String.format(Locale.KOREA, "%,d", maxWon / 10_000)
    return "${min}만 원 ~ ${max}만 원"
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun RecruitmentDetailScreenPreview() {
    RecruitmentDetailScreen(
        uiState = RecruitmentDetailScreenUiState.Success(
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
                // TODO: API 연동 시 ISO 8601 → 한글 날짜 포맷 변환 필요
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
            )
        ),
        onBack = {}, onNavigateToCompetition = {}, onNavigateToAnalysis = {}, onToggleBookmark = {}
    )
}
