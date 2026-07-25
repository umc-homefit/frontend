package com.umc.homefit.presentation.analysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CardBorderColor = Color(0xFFD2D9E2)
private val EditLinkColor = Color(0xFF3C45F3)
private val LabelTextColor = Color(0xFF4A4F55)
private val ValueTextColor = Color(0xFF919AA4)


data class FinanceInfoSection(
    val title: String,
    val step: FinancialInfoStep,
    val rows: List<FinanceInfoRow>
)

data class FinanceInfoRow(
    val label: String,
    val value: String? = null
)

/**
 * 마이페이지(MyFinanceScreenRoute)와 분석 탭(AnalysisScreen)에서 공용으로 쓰는
 * 금융 정보 요약 리스트. 뒤로가기/상단바는 각 화면에서 감싸서 처리하고,
 * 이 컴포저블은 순수 콘텐츠만 담당한다.
 */
@Composable
fun FinancialInfoContent(
    sections: List<FinanceInfoSection>,
    onEditClick: (FinancialInfoStep) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 42.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(sections, key = { it.title }) { section ->
            FinanceInfoCard(
                section = section,
                onEditClick = { onEditClick(section.step) }
            )
        }
    }
}

@Composable
private fun FinanceInfoCard(
    section: FinanceInfoSection,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = section.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "수정",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = EditLinkColor,
                modifier = Modifier.clickable(onClick = onEditClick)
            )
        }

        Spacer(modifier = Modifier.height(17.5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CardBorderColor)
        )

        Spacer(modifier = Modifier.height(18.dp))

        section.rows.forEachIndexed { index, row ->
            if (row.value != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = row.label,
                        fontSize = 14.sp,
                        color = LabelTextColor
                    )
                    Text(
                        text = row.value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ValueTextColor
                    )
                }
            } else {
                Text(
                    text = row.label,
                    fontSize = 14.sp,
                    color = LabelTextColor
                )
            }

            if (index != section.rows.lastIndex) {
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}
