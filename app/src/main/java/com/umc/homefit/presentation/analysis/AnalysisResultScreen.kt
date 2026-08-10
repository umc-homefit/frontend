package com.umc.homefit.presentation.analysis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.presentation.component.AppTopBar
import com.umc.homefit.presentation.component.AutoDismissInfoSnackbar
import com.umc.homefit.presentation.component.TopBarAction
import com.umc.homefit.util.ImageSaveUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val BorderColor = Color(0xFFD2D9E2)

@Composable
fun AnalysisResultScreenRoute(
    viewModel: AnalysisResultScreenViewModel,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToRecommendedProduct: () -> Unit,
    modifier: Modifier = Modifier,
    fromRecord: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalysisResultScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToHome = onNavigateToHome,
        onNavigateToRecommendedProduct = onNavigateToRecommendedProduct,
        fromRecord = fromRecord,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisResultScreen(
    uiState: AnalysisResultScreenUiState,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToRecommendedProduct: () -> Unit,
    modifier: Modifier = Modifier,
    fromRecord: Boolean = false
) {
    var isSavingImage by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var isSnackbarError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    suspend fun saveResultImage() {
        isSavingImage = true
        val saved = try {
            val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
            withContext(Dispatchers.IO) {
                ImageSaveUtil.saveBitmapToGallery(
                    context = context,
                    bitmap = bitmap,
                    displayName = "HomeFit_입주분석결과_${System.currentTimeMillis()}"
                )
            }
        } catch (e: Exception) {
            false
        }
        isSavingImage = false
        isSnackbarError = !saved
        snackbarMessage = if (saved) "입주 분석 결과 이미지가 저장되었습니다" else "이미지 저장에 실패했습니다"
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            coroutineScope.launch { saveResultImage() }
        } else {
            isSnackbarError = true
            snackbarMessage = "저장 권한이 필요합니다"
        }
    }

    fun onShareClick() {
        val data = (uiState as? AnalysisResultScreenUiState.Success)?.data ?: return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, data.shareText)
        }
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun onSaveClick() {
        if (isSavingImage) return

        val needsPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED

        if (needsPermission) {
            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            coroutineScope.launch { saveResultImage() }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "입주 분석 결과",
                showBackButton = true,
                onBackClick = onBack,
                actions = listOf(
                    TopBarAction(
                        icon = painterResource(id = R.drawable.ic_top_share),
                        contentDescription = "공유",
                        onClick = ::onShareClick
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
                        .padding(horizontal = 16.dp, vertical = 21.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, Color(0xFF3C45F3)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3C45F3))
                    ) {
                        Text("홈으로", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = if (fromRecord) ::onSaveClick else onNavigateToRecommendedProduct,
                        enabled = !isSavingImage,
                        modifier = Modifier
                            .weight(1.7f)
                            .height(48.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF3C45F3),
                                        Color(0xFF3C45F3).copy(alpha = 0.5f)
                                    )
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = if (fromRecord) {
                                if (isSavingImage) "저장 중..." else "저장하기"
                            } else {
                                "추천 금융상품 보기"
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
        ) {
            when (uiState) {
                is AnalysisResultScreenUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF3C45F3)
                    )
                }
                is AnalysisResultScreenUiState.Error -> {
                    Text(
                        text = "Error: ${uiState.message}",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
                is AnalysisResultScreenUiState.Success -> {
                    SuccessContent(data = uiState.data)

                    // 갤러리 저장용 캡처 대상
                    if (fromRecord) {
                        Box(
                            modifier = Modifier
                                .offset(x = 4000.dp)
                                .width(maxWidth)
                                .layout { measurable, constraints ->
                                    val unboundedConstraints = constraints.copy(maxHeight = Constraints.Infinity)
                                    val placeable = measurable.measure(unboundedConstraints)
                                    layout(placeable.width, placeable.height) {
                                        placeable.placeRelative(0, 0)
                                    }
                                }
                                .drawWithContent {
                                    graphicsLayer.record { this@drawWithContent.drawContent() }
                                }
                                .background(Color.White)
                        ) {
                            SuccessContent(data = uiState.data, scrollable = false, forceExpanded = true)
                        }
                    }
                }
            }

            AutoDismissInfoSnackbar(
                visible = snackbarMessage != null,
                message = snackbarMessage ?: "",
                isError = isSnackbarError,
                onDismiss = { snackbarMessage = null },
                bottomPadding = 12.dp
            )
        }
    }
}

@Composable
private fun SuccessContent(
    data: AnalysisResultData,
    scrollable: Boolean = true,
    forceExpanded: Boolean = false
) {
    var isAccordionExpanded by remember { mutableStateOf(forceExpanded) }

    Column(
        modifier = Modifier
            .then(if (scrollable) Modifier.fillMaxSize().verticalScroll(rememberScrollState()) else Modifier.fillMaxWidth())
            .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 8.dp)
    ) {
        // 입주 가능성 카드
        Column {
            Text(
                "입주 가능성",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF161616)
            )
            Spacer(modifier = Modifier.height(17.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, BorderColor),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(164.dp)
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
                            .offset(x = (-16).dp, y = (-2).dp)
                    )

                    // 작은 원
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_dot_small),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(x = 108.dp, y = 32.dp)
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_dot_small),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = (-35).dp, y = 24.dp)
                            .size(7.dp)
                    )

                    // 왼쪽 텍스트
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 26.dp)
                    ) {
                        // percentileText는 score 기반 클라이언트 산출값이라 항상 채워짐 (0~100점 유효 범위 내)
                        Surface(
                            shape = RoundedCornerShape(200.dp),
                            color = Color(0xFFF1F0FF)
                        ) {
                            Text(
                                text = data.percentileText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF636AF5)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = data.probabilityGrade,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF636AF5)
                        )
                    }

                    // 집
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 20.dp)
                            .offset(x = 16.dp, y = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_analysis_house),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(width = 220.dp, height = 138.dp)
                        )

                        Text(
                            text = "${data.score}점",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = 6.dp),
                            fontSize = 43.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // 예상 비용 카드
        Column {
            Text(
                "예상 비용",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF161616)
            )
            Spacer(modifier = Modifier.height(17.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "보증금",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF919AA4)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.expectedDeposit,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A4F55)
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .padding(horizontal = 20.dp),
                        color = BorderColor
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "월세",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF919AA4)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.expectedMonthlyRent,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A4F55)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 조건 충족 현황 리스트
        Column {
            Text(
                "조건 충족 현황",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF161616)
            )
            Spacer(modifier = Modifier.height(17.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Column {
                    data.criteriaStatus.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4A4F55)
                            )
                            Text(
                                text = item.statusText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = when (item.resultStatus) {
                                    "PASS" -> Color(0xFF299251)
                                    "FAIL" -> Color(0xFFE53E3E)
                                    else -> Color(0xFFFFC300)
                                }
                            )
                        }
                        if (index != data.criteriaStatus.lastIndex) {
                            HorizontalDivider(color = BorderColor)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(34.dp))

        // 분석 기준 보기 아코디언 드롭다운
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isAccordionExpanded = !isAccordionExpanded },
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "입주 분석 기준",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF919AA4)
                )
                Icon(
                    imageVector = if (isAccordionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF919AA4)
                )
            }
            if (isAccordionExpanded) {
                HorizontalDivider(color = BorderColor)

                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = "입력 정보",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4F55)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 조건 프로필 API(FinancialInfo 입력값) 기반, 6개 항목 항상 채워짐
                    data.inputInfoRows.forEach { row ->
                        AnalysisInfoRow(row.title, row.value)
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    HorizontalDivider(color = BorderColor)

                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = "산정 기준",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4F55)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 적용 기준일(analyzedAt) / 공급 유형(targetType 한글 매핑) / 전용 면적
                    // 신청 유형·신청 순위·비교 공고·전환 이율은 대응 API 필드가 없어 제외
                    data.criteriaInfoRows.forEach { row ->
                        AnalysisInfoRow(row.title, row.value)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 유의사항 영역
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 21.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "*유의사항",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE53E3E).copy(alpha = 0.5f)
                )
                Text(
                    text = "위 결과는 입력하신 정보를 기반으로 산출한 예상 결과입니다.\n실제 심사 결과와 다를 수 있습니다.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 15.sp,
                    color = Color(0xFFE53E3E).copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun AnalysisInfoRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4A4F55)
        )

        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF919AA4)
        )
    }
}
