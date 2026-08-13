package com.umc.homefit.presentation.finance

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umc.homefit.R
import com.umc.homefit.presentation.component.TopBarAction
import com.umc.homefit.util.toWonText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.PlatformTextStyle
import com.umc.homefit.presentation.finance.component.HelpTerm
import com.umc.homefit.presentation.finance.component.TermsHelpDialog
import androidx.compose.runtime.setValue
import com.umc.homefit.presentation.component.AppScaffold
import coil.compose.AsyncImage

@Composable
fun ProductDetailScreenRoute(
    productId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(productId) {
        viewModel.loadProductDetail(
            productId = productId
        )
    }

    when (val state = uiState) {
        ProductDetailScreenUiState.Loading -> {
            ProductDetailLoadingContent(
                onBackClick = onBack,
                modifier = modifier
            )
        }

        is ProductDetailScreenUiState.Success -> {
            ProductDetailScreen(
                product = state.product,
                onBackClick = onBack,
                onShareClick = {
                    val shareText = buildString {
                        appendLine(state.product.productName)
                        appendLine("금리 ${state.product.rateRange}")

                        state.product.officialUrl?.let { url ->
                            append(url)
                        }
                    }

                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"

                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            state.product.productName
                        )

                        putExtra(
                            Intent.EXTRA_TEXT,
                            shareText
                        )
                    }

                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "금융상품 공유"
                        )
                    )
                },
                onConsultClick = {
                    // 상담 문의 화면 이동
                },
                onApplyClick = {
                    state.product.officialUrl?.let { url ->
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )

                        context.startActivity(intent)
                    }
                },
                modifier = modifier
            )
        }

        is ProductDetailScreenUiState.Error -> {
            ProductDetailErrorContent(
                message = state.message,
                onBackClick = onBack,
                onRetryClick = viewModel::retry,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ProductDetailLoadingContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = null,
        modifier = modifier,
        showBackButton = true,
        onBackClick = onBackClick,
        showDivider = true
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                color = Color(0xFF5A5FF5),
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
private fun ProductDetailErrorContent(
    message: String,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = null,
        modifier = modifier,
        showBackButton = true,
        onBackClick = onBackClick,
        showDivider = true
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF))
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = Color(0xFF4A4F55),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            TextButton(
                onClick = onRetryClick
            ) {
                Text(
                    text = "다시 시도",
                    color = Color(0xFF5A5FF5),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProductDetailScreen(
    product: ProductDetailData,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onConsultClick: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showTermsHelpDialog by rememberSaveable {
        mutableStateOf(false)
    }

    AppScaffold(
        title = null,
        modifier = modifier,
        showBackButton = true,
        onBackClick = onBackClick,
        actions = listOf(
            TopBarAction(
                icon = painterResource(
                    id = R.drawable.ic_top_share
                ),
                contentDescription = "상품 공유",
                onClick = onShareClick,
                iconSize = 28.dp
            )
        ),
        showDivider = true,
        bottomBar = {
            ProductDetailBottomBar(
                applyEnabled = !product.officialUrl.isNullOrBlank(),
                onConsultClick = onConsultClick,
                onApplyClick = onApplyClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF)),
            contentPadding = PaddingValues(
                bottom = 16.dp
            )
        ) {
            item {
                ProductDetailHeader(
                    product = product
                )
            }

            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFF0F4F9)
                )
            }

            item {
                ProductDetailSection(
                    title = "금리 안내"
                ) {
                    DetailInformationBox {
                        DetailValueRow(
                            label = "기본 금리",
                            value = product.rateRange.toAnnualRateText(),
                            valueColor = Color(0xFF3C45F3),
                            valueFontWeight = FontWeight.Bold
                        )

                        DetailValueRow(
                            label = "우대 금리",
                            value = product.preferentialRateDiscount?.let { discount ->
                                "최대 ${formatDecimal(discount)}%p 할인"
                            } ?: "정보 없음"
                        )

                        DetailValueRow(
                            label = "생애최초 우대",
                            value = product.firstTimeBuyerRateDiscount?.let { discount ->
                                "${formatDecimal(discount)}%p 추가할인"
                            } ?: "정보 없음"
                        )

                        Text(
                            text = "*금리는 신청일 및 심사 결과에 따라 변동될 수 있습니다",
                            color = Color(0xFFC7D0DA),
                            fontSize = 13.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            item {
                ProductDetailSection(
                    title = "대출 한도 안내",
                    showHelpIcon = true,
                    onHelpClick = {
                        showTermsHelpDialog = true
                    }
                ) {
                    DetailInformationBox {
                        DetailValueRow(
                            label = "최대 대출 한도",
                            value = product.maxLimitAmount?.let { amount ->
                                "최대 ${amount.toWonText()}"
                            } ?: "정보 없음"
                        )

                        DetailValueRow(
                            label = "LTV 한도",
                            value = product.ltvRatio?.let { ratio ->
                                "담보가치의 $ratio%"
                            } ?: "정보 없음"
                        )

                        DetailValueRow(
                            label = "DTI 한도",
                            value = product.dtiRatio?.let { ratio ->
                                "소득의 $ratio% 이하"
                            } ?: "정보 없음"
                        )

                        DetailValueRow(
                            label = "대출 기간",
                            value = loanTermText(product) ?: "정보 없음"
                        )

                        Text(
                            text = "*한도는 소득, 담보 가치, 신용도에 따라 달라질 수 있습니다",
                            color = Color(0xFFC7D0DA),
                            fontSize = 13.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            item {
                ProductDetailSection(
                    title = "필요 서류 안내"
                ) {
                    RequiredDocumentsBox(
                        documents = product.requiredDocuments
                    )
                }
            }
        }
    }

    if (showTermsHelpDialog) {
        TermsHelpDialog(
            terms = listOf(
                HelpTerm(
                    title = "LTV (주택담보대출비율)",
                    description = "집값 대비 얼마나 대출받을 수 있는지를 나타내는 비율"
                ),
                HelpTerm(
                    title = "DTI (총부채상환비율)",
                    description = "연소득 대비 대출 원리금 상환액이 차지하는 비율"
                )
            ),
            onDismissRequest = {
                showTermsHelpDialog = false
            }
        )
    }
}
@Composable
private fun ProductDetailHeader(
    product: ProductDetailData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = product.providerLogoUrl,
            contentDescription = product.productName,
            placeholder = painterResource(R.drawable.ic_mypage_bank),
            error = painterResource(R.drawable.ic_mypage_bank),
            fallback = painterResource(R.drawable.ic_mypage_bank),
            modifier = Modifier
                .offset(y = 6.dp)
                .size(58.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.productName,
                color = Color(0xFF18191B),
                fontSize = 20.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProductDetailTag(
                    text = product.providerType
                )

                if (product.requireNoHouse) {
                    ProductDetailTag(
                        text = "무주택자"
                    )
                }

                if (product.firstTimeBuyerOnly) {
                    ProductDetailTag(
                        text = "생애최초"
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailSection(
    title: String,
    modifier: Modifier = Modifier,
    showHelpIcon: Boolean = false,
    onHelpClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 15.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = Color(0xFF18191B),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (showHelpIcon) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4F9))
                        .clickable(onClick = onHelpClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        modifier = Modifier.offset(y = (-1).dp),
                        color = Color(0xFF8793A2),
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = LocalTextStyle.current.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        content()
    }
}

@Composable
private fun DetailInformationBox(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFD8E0E8),
                shape = RoundedCornerShape(3.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(13.dp),
        content = content
    )
}

@Composable
private fun DetailValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color(0xFF919AA4),
    valueFontWeight: FontWeight = FontWeight.Normal
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color(0xFF4A4F55),
            fontSize = 13.sp,
            lineHeight = 15.sp
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            lineHeight = 15.sp,
            fontWeight = valueFontWeight,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun RequiredDocumentsBox(
    documents: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFD8E0E8),
                shape = RoundedCornerShape(3.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = "공통 서류",
            color = Color(0xFF4A4F55),
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (documents.isEmpty()) {
            Text(
                text = "정보 없음",
                color = Color(0xFF919AA4),
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
        } else {
            documents.forEach { document ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        color = Color(0xFFA9B6C5),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = document,
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF919AA4),
                        fontSize = 13.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "*서류는 신청 시점에 따라 변경될 수 있으니 은행에 확인하세요",
            color = Color(0xFFC7D0DA),
            fontSize = 13.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun ProductDetailTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFFFFFFF),
        shape = RoundedCornerShape(120.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD8E0E8)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 5.dp
            ),
            color = Color(0xFF919AA4),
            fontSize = 14.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun ProductDetailBottomBar(
    applyEnabled: Boolean,
    onConsultClick: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFFFFFFF),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onConsultClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFF5A5FF5)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFFFFFFF),
                    contentColor = Color(0xFF5A5FF5)
                ),
                contentPadding = PaddingValues(
                    horizontal = 8.dp
                )
            ) {
                Text(
                    text = "상담 문의",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Button(
                onClick = onApplyClick,
                enabled = applyEnabled,
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF3C45F3),
                                Color(0x803C45F3)
                            )
                        )
                    ),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFFFFFFF),
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color(0xFF919AA4)
                ),
                contentPadding = PaddingValues(
                    horizontal = 8.dp
                )
            ) {
                Text(
                    text = "은행 앱에서 신청하기",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

private fun formatDecimal(
    value: Double
): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}

private fun loanTermText(
    product: ProductDetailData
): String? {
    val minYears = product.loanTermMinYears
    val maxYears = product.loanTermMaxYears

    return when {
        minYears != null && maxYears != null ->
            "${minYears}~${maxYears}년 선택 가능"

        minYears != null ->
            "최소 ${minYears}년"

        maxYears != null ->
            "최대 ${maxYears}년"

        else -> null
    }
}
