package com.umc.homefit.presentation.recruitment

import android.content.Intent
import android.widget.Toast
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.umc.homefit.R
import com.umc.homefit.presentation.recruitment.component.RecruitmentTabRow
import com.umc.homefit.presentation.component.AppScaffold
import com.umc.homefit.presentation.component.PreparingStateView
import com.umc.homefit.presentation.component.TopBarAction
import com.umc.homefit.presentation.theme.AnalysisButtonGradient
import com.umc.homefit.presentation.theme.BackgroundLight
import com.umc.homefit.presentation.theme.Black
import com.umc.homefit.presentation.theme.BrightGray
import com.umc.homefit.presentation.theme.DarkGray
import com.umc.homefit.presentation.theme.Gray
import com.umc.homefit.presentation.theme.LightBlue
import com.umc.homefit.presentation.theme.LightGray
import com.umc.homefit.presentation.theme.LightRed
import com.umc.homefit.presentation.theme.Main
import com.umc.homefit.presentation.theme.RecruitmentAccent
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.Red
import com.umc.homefit.presentation.theme.StatusClosingSoonText
import com.umc.homefit.presentation.theme.Sub
import com.umc.homefit.presentation.theme.TextBlack
import com.umc.homefit.presentation.theme.White
import com.umc.homefit.util.error.ErrorCode

@Composable
fun RecruitmentDetailScreenRoute(
    viewModel: RecruitmentDetailScreenViewModel,
    analysisId: String?,
    onBack: () -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    onNavigateToAnalysisResult: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RecruitmentDetailScreen(
        uiState = uiState,
        analysisId = analysisId,
        onBack = onBack,
        onNavigateToAnalysis = onNavigateToAnalysis,
        onNavigateToAnalysisResult = onNavigateToAnalysisResult,
        onToggleBookmark = viewModel::toggleBookmark,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

@Composable
fun RecruitmentDetailScreen(
    uiState: RecruitmentDetailScreenUiState,
    analysisId: String?,
    onBack: () -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    onNavigateToAnalysisResult: (String) -> Unit,
    onToggleBookmark: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBookmarked = (uiState as? RecruitmentDetailScreenUiState.Success)?.recruitment?.isSaved == true
    val context = LocalContext.current

    AppScaffold(
        title = null,
        showBackButton = true,
        onBackClick = onBack,
        actions = listOf(
            TopBarAction(
                icon = painterResource(
                    id = if (isBookmarked) R.drawable.ic_top_save_active else R.drawable.ic_top_save
                ),
                contentDescription = if (isBookmarked) "찜 해제" else "찜하기",
                onClick = onToggleBookmark
            ),
            TopBarAction(
                icon = painterResource(id = R.drawable.ic_top_share),
                contentDescription = "공유",
                onClick = {
                    (uiState as? RecruitmentDetailScreenUiState.Success)?.recruitment?.let { recruitment ->
                        val shareText = buildString {
                            appendLine(recruitment.title)
                            appendLine("위치 | ${recruitment.supplyLocation}")
                            appendLine("보증금 | ${recruitment.depositRangeText}")
                            appendLine("월 임대료 | ${recruitment.monthlyRentRangeText}")
                            appendLine("청약접수 | ${recruitment.applicationStartText} ~ ${recruitment.applicationEndText}")
                            if (recruitment.sourceUrl.isNotBlank()) {
                                append(recruitment.sourceUrl)
                            }
                        }
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, recruitment.title)
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "공고 공유"))
                    }
                }
            )
        ),
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            when (uiState) {
                is RecruitmentDetailScreenUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is RecruitmentDetailScreenUiState.Success -> {
                    RecruitmentDetailContent(
                        recruitment = uiState.recruitment,
                        analysisId = analysisId,
                        onNavigateToAnalysis = onNavigateToAnalysis,
                        onNavigateToAnalysisResult = onNavigateToAnalysisResult
                    )
                }
                is RecruitmentDetailScreenUiState.Error -> {
                    if (uiState.errorCode == ErrorCode.COMMON404) {
                        // 공고 데이터 자체가 없는 경우: 재시도로 해결되지 않으므로 준비 중 안내
                        PreparingStateView()
                    } else {
                        // 네트워크 오류 등 일시적 실패: 재시도 가능한 에러 화면
                        RecruitmentErrorView(message = uiState.message, onRetry = onRetry)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecruitmentErrorView(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, fontSize = 14.sp, color = Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "다시 시도",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Main,
                modifier = Modifier.clickable(onClick = onRetry)
            )
        }
    }
}

@Composable
private fun RecruitmentDetailContent(
    recruitment: RecruitmentDetailUiModel,
    analysisId: String?,
    onNavigateToAnalysis: (String) -> Unit,
    onNavigateToAnalysisResult: (String) -> Unit
) {
    var showFullScreenViewer by remember { mutableStateOf(false) }
    var selectedPhotoIndex by remember { mutableIntStateOf(0) }
    // 0: 공고 상세, 1: 경쟁률 (백엔드 경쟁률 API 준비 전까지 준비 중 안내만 표시)
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        DetailTitleSection(recruitment)
        RecruitmentTabRow(
            selectedTabIndex = selectedTab,
            onTabClick = { index -> selectedTab = index }
        )

        Box(modifier = Modifier.weight(1f)) {
            if (selectedTab == 1) {
                PreparingStateView()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(rows = supplyInfoRows(recruitment))

                    PhotoGrid(
                        photoUrls = recruitment.photoUrls,
                        onPhotoClick = { index ->
                            selectedPhotoIndex = index
                            showFullScreenViewer = true
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column {
                        SectionTitle("자격 조건")
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoCard(rows = qualificationRows(recruitment))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column {
                        SectionTitle("신청 기간")
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoCard(rows = applicationPeriodRows(recruitment))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SectionTitle("첨부 파일 및 안내 자료")

                        recruitment.attachments.forEach { attachment ->
                            AttachmentItem(
                                fileName = attachment.fileName,
                                registeredDate = attachment.registeredDateText,
                                fileType = attachment.fileType,
                                onClick = {
                                    runCatching {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(attachment.fileUrl)))
                                    }.onFailure {
                                        Toast.makeText(context, "파일을 열 수 없습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }

                        Text(
                            text = "*경쟁률 정보를 함께 확인하면 청약 전략 수립에 도움이 됩니다.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = StatusClosingSoonText.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        BottomButtonBar(
            analysisId = analysisId,
            onCompetitionClick = { selectedTab = 1 },
            onAnalysisClick = { onNavigateToAnalysis(recruitment.noticeId.toString()) },
            onAnalysisResultClick = onNavigateToAnalysisResult
        )
    }

    if (showFullScreenViewer) {
        FullScreenPhotoViewer(
            photoUrls = recruitment.photoUrls,
            initialPage = selectedPhotoIndex,
            onDismiss = { showFullScreenViewer = false }
        )
    }
}

@Composable
private fun DetailTitleSection(recruitment: RecruitmentDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight)
            .drawBehind {
                drawLine(
                    color = BrightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(start = 16.dp, end = 16.dp, top = 31.dp, bottom = 31.dp)
    ) {
        Text(
            text = recruitment.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DetailStatusBadge(status = recruitment.status, label = recruitment.statusDisplayText)
            recruitment.targetTypeBadgeText?.let { TargetTypeBadge(label = it) }
        }
    }
}

@Composable
private fun TargetTypeBadge(label: String) {
    Box(
        modifier = Modifier
            .background(White, RoundedCornerShape(120.dp))
            .border(BorderStroke(1.dp, LightGray), RoundedCornerShape(120.dp))
            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray)
    }
}

@Composable
private fun DetailStatusBadge(status: String, label: String) {
    val (background, text) = when (status) {
        "RECRUITING" -> LightBlue to Sub
        "SCHEDULED" -> BrightGray to DarkGray
        "CLOSING_SOON" -> LightRed to Red
        else -> BrightGray to DarkGray
    }
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(120.dp))
            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = text)
    }
}

private fun supplyInfoRows(recruitment: RecruitmentDetailUiModel) = listOf(
    "공급 위치" to recruitment.supplyLocation,
    "공급 유형" to recruitment.supplyType,
    "공급 세대수" to recruitment.unitSummary,
    "임대 보증금" to recruitment.depositRangeText,
    "월 임대료" to recruitment.monthlyRentRangeText,
    "입주 예정일" to recruitment.moveInDate
)

private fun qualificationRows(recruitment: RecruitmentDetailUiModel) = listOf(
    "연령" to recruitment.ageRange,
    "소득 기준" to recruitment.incomeStandard,
    "자산 기준" to recruitment.assetStandard,
    "주택 소유" to recruitment.housingOwnership,
    "거주지 요건" to recruitment.residencyRequirement
)

private fun applicationPeriodRows(recruitment: RecruitmentDetailUiModel) = listOf(
    "접수 시작" to recruitment.applicationStartText,
    "접수 마감" to recruitment.applicationEndText,
    "당첨자 발표" to recruitment.winnerAnnouncementDate,
    "계약 체결" to recruitment.contractPeriod
)

@Composable
private fun InfoCard(rows: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, LightGray), RoundedCornerShape(4.dp))
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
                    color = DarkGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
}

@Composable
private fun PhotoGrid(photoUrls: List<String>, onPhotoClick: (Int) -> Unit) {
    when {
        photoUrls.isEmpty() -> NoPhotoPlaceholder()
        photoUrls.size < 4 -> SinglePhotoBox(url = photoUrls[0], onPhotoClick = onPhotoClick)
        else -> PhotoQuadrantGrid(photoUrls = photoUrls, onPhotoClick = onPhotoClick)
    }
}

@Composable
private fun NoPhotoPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(BrightGray)
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = RecruitmentBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = RecruitmentBorder,
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            }
    )
}

// 사진이 1~3장뿐일 때: 첫 번째 사진을 꽉 채워 보여주고, 돋보기로 전체(1~3장)를 슬라이드해서 볼 수 있게 함
@Composable
private fun SinglePhotoBox(url: String, onPhotoClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(4.dp))
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { onPhotoClick(0) })
        )

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

// 사진이 4장 이상일 때: 2x2 그리드로 앞 4장만 보여주고, 돋보기로 전체를 슬라이드해서 볼 수 있게 함
@Composable
private fun PhotoQuadrantGrid(photoUrls: List<String>, onPhotoClick: (Int) -> Unit) {
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
                PhotoCell(url = photoUrls.getOrNull(0), onClick = { onPhotoClick(0) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
                PhotoCell(url = photoUrls.getOrNull(1), onClick = { onPhotoClick(1) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
            }
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()) {
                PhotoCell(url = photoUrls.getOrNull(2), onClick = { onPhotoClick(2) }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
                PhotoCell(url = photoUrls.getOrNull(3), onClick = { onPhotoClick(3) }, modifier = Modifier
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
private fun PhotoCell(url: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.clickable(onClick = onClick)
        )
    } else {
        Box(modifier = modifier.background(BrightGray))
    }
}

@Composable
private fun FullScreenPhotoViewer(
    photoUrls: List<String>,
    initialPage: Int,
    onDismiss: () -> Unit
) {
    if (photoUrls.isEmpty()) return
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
        ) {
            val pagerState = rememberPagerState(initialPage = initialPage) { photoUrls.size }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableImage(url = photoUrls[page])
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
private fun ZoomableImage(url: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    AsyncImage(
        model = url,
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
private fun AttachmentItem(fileName: String, registeredDate: String, fileType: String, onClick: () -> Unit) {
    val (iconPainter, iconTint) = when (fileType) {
        "PDF" -> painterResource(id = R.drawable.ic_recruitment_pdf) to Color(0xFFEF5350)
        "DOC" -> rememberVectorPainter(Icons.Default.Description) to Main
        "LINK" -> rememberVectorPainter(Icons.Default.Link) to Sub
        "IMAGE" -> rememberVectorPainter(Icons.Default.Image) to DarkGray
        else -> rememberVectorPainter(Icons.AutoMirrored.Filled.InsertDriveFile) to Gray
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(BackgroundLight, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, LightGray), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = fileType,
            tint = iconTint,
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
            Text(text = fileName, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Gray)
            Text(text = registeredDate, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = LightGray)
        }
    }
}

@Composable
private fun BottomButtonBar(
    analysisId: String?,
    onCompetitionClick: () -> Unit,
    onAnalysisClick: () -> Unit,
    onAnalysisResultClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(text = "경쟁률", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Main)
        }

        Box(
            modifier = Modifier
                .weight(202f)
                .height(48.dp)
                .background(AnalysisButtonGradient, RoundedCornerShape(4.dp))
                .clickable(onClick = { analysisId?.let(onAnalysisResultClick) ?: onAnalysisClick() }),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (analysisId != null) "입주 분석 결과보기" else "입주 분석 요청하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun RecruitmentDetailScreenPreview() {
    RecruitmentDetailScreen(
        uiState = RecruitmentDetailScreenUiState.Success(
            recruitment = RecruitmentDetailUiModel(
                noticeId = 1,
                title = "강동구 청년안심주택 2025-03호",
                status = "RECRUITING",
                statusDisplayText = "모집중",
                targetTypeBadgeText = "청년",
                isSaved = true,
                sourceUrl = "https://example.com/notices/1",
                supplyLocation = "서울 강동구 천호동 123-4",
                supplyType = "청년안심주택 (임대)",
                unitSummary = "전용 24㎡ 18세대 / 전용 33㎡ 12세대",
                depositRangeText = "3,200만 원 ~ 4,800만 원",
                monthlyRentRangeText = "28만 원 ~ 41만 원",
                moveInDate = "2025년 9월",
                ageRange = "만 19세 ~ 39세",
                incomeStandard = "도시근로자 월평균 소득 100% 이하",
                assetStandard = "총 자산 3억 6,100만 원 이하",
                housingOwnership = "무주택 세대구성원",
                residencyRequirement = "서울시 거주 또는 직장 소재",
                applicationStartText = "2025.06.09",
                applicationEndText = "2025.06.13",
                winnerAnnouncementDate = "공고문 참고",
                contractPeriod = "공고문 참고",
                attachments = listOf(
                    AttachmentRow(fileName = "2025-03호 공고문 (PDF)", registeredDateText = "2025.06.02 등록", fileUrl = "https://example.com/notice.pdf", fileType = "PDF"),
                    AttachmentRow(fileName = "입주자 모집 안내 책자", registeredDateText = "2025.06.02 등록", fileUrl = "https://example.com/guide.docx", fileType = "DOC"),
                    AttachmentRow(fileName = "평면도.jpg", registeredDateText = "2025.06.02 등록", fileUrl = "https://example.com/floorplan.jpg", fileType = "IMAGE")
                ),
                photoUrls = emptyList()
            )
        ),
        analysisId = null,
        onBack = {}, onNavigateToAnalysis = {}, onNavigateToAnalysisResult = {}, onToggleBookmark = {}, onRetry = {}
    )
}
