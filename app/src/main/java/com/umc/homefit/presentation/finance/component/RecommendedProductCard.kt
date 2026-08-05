package com.umc.homefit.presentation.finance.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import com.umc.homefit.R
import com.umc.homefit.data.dto.finance.FinanceProductDto


@Composable
private fun ProductTypeBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
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

@Composable
fun RecommendedProductCard(
    product: FinanceProductDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tags = product.toDisplayTags()

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
            // TODO: 금융기관별 이미지 필드 또는 매핑이 정해지면 변경
            Image(
                painter = painterResource(
                    id = R.drawable.img_shinhan_logo
                ),
                contentDescription = product.productName,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.productName,
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF18191B),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    ProductTypeBadge(
                        text = product.providerType.toDisplayName()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.rateRange,
                    color = Color(0xFF3C45F3),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "최대 ${product.maxLimitAmount.toWonText()}",
                    color = Color(0xFF919AA4),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.toConditionDescription(),
                    color = Color(0xFF919AA4),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tags.take(2).forEach { tag ->
                            ProductTag(text = tag)
                        }
                    }
                }
            }
        }
    }
}

private fun String.toDisplayName(): String {
    return when (this) {
        "POLICY" -> "정책상품"
        "BANK" -> "은행상품"
        else -> this
    }
}

private fun Long.toWonText(): String {
    val billion = 100_000_000L
    val tenThousand = 10_000L

    return when {
        this % billion == 0L ->
            "${this / billion}억원"

        this >= billion ->
            "${this / billion}억 ${(this % billion) / tenThousand}만원"

        else ->
            "${this / tenThousand}만원"
    }
}

private fun FinanceProductDto.toConditionDescription(): String {
    val conditions = buildList {
        maxIncome?.let {
            add("소득 ${it.toWonText()} 이하")
        }

        if (requireNoHouse) {
            add("무주택자")
        }

        if (minAge != null || maxAge != null) {
            val ageCondition = when {
                minAge != null && maxAge != null ->
                    "만 ${minAge}~${maxAge}세"

                minAge != null ->
                    "만 ${minAge}세 이상"

                else ->
                    "만 ${maxAge}세 이하"
            }

            add(ageCondition)
        }

        if (firstTimeBuyerOnly) {
            add("생애최초 구매자")
        }
    }

    return conditions
        .takeIf { it.isNotEmpty() }
        ?.joinToString(" · ")
        ?: providerName
}

private fun FinanceProductDto.toDisplayTags(): List<String> {
    return buildList {
        add(providerName)

        when (productCategory) {
            "JEONSE_LOAN" -> add("전세자금")
            "MORTGAGE_LOAN" -> add("주택담보")
            "SAVINGS" -> add("저축")
            "SUBSCRIPTION" -> add("청약")
        }
    }.distinct()
}

@Preview(
    showBackground = true,
    widthDp = 390
)
@Composable
private fun RecommendedProductCardPreview() {
    RecommendedProductCard(
        product = FinanceProductDto(
            productId = 101L,
            productName = "청년전용 버팀목전세자금",
            providerType = "POLICY",
            productCategory = "JEONSE_LOAN",
            providerName = "주택도시기금",
            rateRange = "1.5% ~ 2.7%",
            maxIncome = 60_000_000L,
            firstTimeBuyerOnly = false,
            maxLimitAmount = 200_000_000L,
            minAge = 19,
            maxAge = 34,
            requireNoHouse = true,
            minMonthlyDeposit = null,
            maxMonthlyDeposit = null,
            isEligible = true,
            ageCheckSkipped = false,
            householdHeadCheckSkipped = false,
            marriedCheckSkipped = false,
            newbornCheckSkipped = false,
            firstTimeBuyerCheckSkipped = false,
            ineligibleReasons = emptyList()
        ),
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}
