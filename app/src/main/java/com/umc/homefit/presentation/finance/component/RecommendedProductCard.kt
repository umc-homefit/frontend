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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.presentation.finance.FinanceRecommendedProductUiModel
import coil.compose.AsyncImage
import com.umc.homefit.presentation.theme.Black
import com.umc.homefit.presentation.theme.BrightGray
import com.umc.homefit.presentation.theme.DarkGray
import com.umc.homefit.presentation.theme.HomeFitTheme
import com.umc.homefit.presentation.theme.Sub
import com.umc.homefit.presentation.theme.White

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
                modifier = Modifier
                    .size(53.dp)
                    .align(Alignment.Top)
                    .clip(RoundedCornerShape(4.dp)),
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
                        color = Black,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    ProductTypeBadge(
                        text = product.productType
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = product.interestRate,
                    color = Sub,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = product.amountDescription,
                    color = Color(0xFF919AA4),
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = product.targetDescription,
                    color = Color(0xFF919AA4),
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )

                if (product.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 7.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        product.tags.take(3).forEach { tag ->
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
        color = White,
        shape = RoundedCornerShape(200.dp),
        border = BorderStroke(
            width = 1.dp,
            color = DarkGray
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            color = DarkGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 14.sp
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
        color = BrightGray,
        shape = RoundedCornerShape(200.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            color = DarkGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 14.sp
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 390
)
@Composable
private fun RecommendedProductCardPreview() {
    HomeFitTheme {
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
}
