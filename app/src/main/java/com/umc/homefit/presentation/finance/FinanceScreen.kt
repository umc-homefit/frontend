package com.umc.homefit.presentation.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.Locale
import com.umc.homefit.data.dto.finance.FinanceMatchResultDto
import com.umc.homefit.data.dto.finance.FinanceProductDto
import com.umc.homefit.presentation.finance.component.RecommendedProductCard

@Composable
fun FinanceScreenRoute(
    viewModel: FinanceScreenViewModel,
    onNavigateToRecommendedProducts: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FinanceScreen(
        uiState = uiState,
        onNavigateToRecommendedProducts = onNavigateToRecommendedProducts,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@Composable
fun FinanceScreen(
    uiState: FinanceScreenUiState,
    onNavigateToRecommendedProducts: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is FinanceScreenUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FinanceScreenUiState.Success -> {
            FinanceSuccessContent(
                result = uiState.result,
                onNavigateToRecommendedProducts =
                    onNavigateToRecommendedProducts,
                onNavigateToDetail = onNavigateToDetail,
                modifier = modifier
            )
        }

        is FinanceScreenUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${uiState.message}",
                    color = Color(0xFFE53935)
                )
            }
        }
    }
}

@Composable
private fun FinanceSuccessContent(
    result: FinanceMatchResultDto,
    onNavigateToRecommendedProducts: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            FinanceHeaderSection(
                matchedCount = result.matchedCount,
                minRate = result.minRate,
                maxLimitAmount = result.maxLimitAmount
            )
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFFF0F4F9))
            )
        }

        item {
            Text(
                text = "추천 상품",
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 20.dp,
                    end = 16.dp,
                    bottom = 14.dp
                ),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            RecommendedProductsSection(
                // API 응답 순서대로 최대 3개만 표시
                products = result.products.take(3),
                onProductClick = onNavigateToDetail
            )
        }

        item {
            RecommendedProductsButton(
                onClick = onNavigateToRecommendedProducts,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun FinanceHeaderSection(
    matchedCount: Int,
    minRate: String,
    maxLimitAmount: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 22.dp
            )
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "나의 소득 · 자산 조건 기준 추천 결과입니다.",
            color = Color(0xFF919AA4),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        FinanceSummaryCard(
            matchedCount = matchedCount,
            minRate = minRate,
            maxLimitAmount = maxLimitAmount
        )
    }
}

@Composable
private fun FinanceSummaryCard(
    matchedCount: Int,
    minRate: String,
    maxLimitAmount: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(95.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF0F4F9)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FinanceSummaryItem(
            label = "추천 상품",
            value = "${matchedCount}가지",
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.height(36.dp),
            color = Color(0xFFDCE2E9)
        )

        FinanceSummaryItem(
            label = "최저 금리",
            value = minRate,
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.height(36.dp),
            color = Color(0xFFDCE2E9)
        )

        FinanceSummaryItem(
            label = "한도",
            value = formatLimitAmount(maxLimitAmount),
            modifier = Modifier.weight(1f)
        )
    }
}

private fun formatLimitAmount(amount: Long): String {
    val oneHundredMillion = 100_000_000L

    return if (amount >= oneHundredMillion &&
        amount % oneHundredMillion == 0L
    ) {
        "최대 ${amount / oneHundredMillion}억"
    } else {
        "최대 ${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원"
    }
}

@Composable
private fun FinanceSummaryItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                color = Color(0xFF919AA4),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                color = Color(0xFF4A4F55),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RecommendedProductsSection(
    products: List<FinanceProductDto>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val eligibleProducts = products
        .filter { product -> product.isEligible }
        .take(3)

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            eligibleProducts.forEach { product ->
                RecommendedProductCard(
                    product = product,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = {
                        onProductClick(product.productId)
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x00FFFFFF),
                            Color(0xAAFFFFFF),
                            Color.White
                        )
                    )
                )
        )
    }
}

@Composable
private fun RecommendedProductsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF3C45F3),
                        Color(0x803C45F3)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "추천 상품 전체 보기",
            color = Color(0xFFFFFFFF),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun FinanceScreenPreview() {
    FinanceScreen(
        uiState = FinanceScreenUiState.Success(
            result = FinanceMatchResultDto(
                matchedCount = 2,
                minRate = "1.0%",
                maxLimitAmount = 500_000_000L,
                products = emptyList()
            )
        ),
        onNavigateToRecommendedProducts = {},
        onNavigateToDetail = {}
    )
}
