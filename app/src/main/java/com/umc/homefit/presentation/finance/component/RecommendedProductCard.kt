package com.umc.homefit.presentation.finance.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.data.dto.home.RecommendedProductDto
import com.umc.homefit.presentation.finance.FinanceRecommendedProductUiModel
import coil.compose.AsyncImage

@Composable
fun RecommendedProductCard(
    product: RecommendedProductDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecommendedProductCard(
        product = FinanceRecommendedProductUiModel(
            productId = product.productId,
            title = product.title,
            productType = product.productType,
            interestRate = product.interestRate,
            amountDescription = product.amountDescription,
            targetDescription = product.targetDescription,
            tags = product.tags
        ),
        onClick = onClick,
        modifier = modifier,
        iconRes = product.iconRes
    )
}

@Composable
fun RecommendedProductCard(
    product: FinanceRecommendedProductUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.ic_mypage_bank
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD8E0E8)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = product.providerLogoUrl,
                contentDescription = product.title,
                modifier = Modifier.size(56.dp),
                placeholder = painterResource(id = iconRes),
                error = painterResource(id = iconRes),
                fallback = painterResource(id = iconRes),
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.title,
                        color = Color(0xFF18191B),
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    ProductTypeBadge(
                        text = product.productType
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.interestRate,
                    color = Color(0xFF3C45F3),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 13.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.amountDescription,
                    color = Color(0xFF919AA4),
                    fontSize = 13.sp,
                    lineHeight = 13.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.targetDescription,
                    color = Color(0xFF919AA4),
                    fontSize = 13.sp,
                    lineHeight = 13.sp
                )

                if (product.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        product.tags.take(2).forEach { tag ->
                            ProductTag(
                                text = tag
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductTypeBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFFFFFFF),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFF6F7782)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 1.dp
            ),
            color = Color(0xFF4A4F55),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ProductTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF0F4F9),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 2.dp
            ),
            color = Color(0xFF69727D),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 390
)
@Composable
private fun RecommendedProductCardPreview() {
    RecommendedProductCard(
        product = FinanceRecommendedProductUiModel(
            productId = 106,
            title = "주택청약종합저축",
            productType = "정부지원",
            interestRate = "연 최대 4.50%",
            amountDescription = "월 납입 | 최대 50만 원",
            targetDescription = "가입대상 | 무주택 청년",
            tags = listOf(
                "청약",
                "소득공제"
            )
        ),
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}
