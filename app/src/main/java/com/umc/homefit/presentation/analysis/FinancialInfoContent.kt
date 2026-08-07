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
import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.util.mapToHouseOption
import com.umc.homefit.util.toAnnualIncomeText
import java.text.NumberFormat
import java.util.Locale

private val CardBorderColor = Color(0xFFD2D9E2)
private val EditLinkColor = Color(0xFF636AF5)
private val LabelTextColor = Color(0xFF4A4F55)
private val ValueTextColor = Color(0xFF919AA4)
private const val NOT_ENTERED_LABEL = "정보 없음"
private val KOREAN_NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.KOREA)


data class FinanceInfoSection(
    val title: String,
    val step: FinancialInfoStep,
    val rows: List<FinanceInfoRow>
)

data class FinanceInfoRow(
    val label: String,
    val value: String? = null
)

/** "원" 단위 금액을 "OOO만 원" 형태(콤마 포함)로 표시용 포맷한다. */
private fun formatManWon(wonAmount: Long): String =
    "${KOREAN_NUMBER_FORMAT.format(wonAmount / 10_000)}만 원"

/**
 * GET /api/users/me/condition-profile 응답을 마이페이지/분석 탭 요약 리스트용 섹션으로 변환한다.
 * (IncomeStep에서 소득 유형 입력이 빠졌으므로 소득 정보는 "연간 총소득" 한 행만 보여준다)
 */
fun ConditionProfileResponse.toFinanceInfoSections(): List<FinanceInfoSection> = listOf(
    FinanceInfoSection(
        title = "소득 정보",
        step = FinancialInfoStep.INCOME,
        rows = listOf(
            // toAnnualIncomeText()로 계산한 값(반올림 처리됨)에 표시용 콤마 포맷만 더한다.
            FinanceInfoRow("연간 총소득", "${KOREAN_NUMBER_FORMAT.format(toAnnualIncomeText(monthlyIncomeAmount).toLong())}만 원")
        )
    ),
    FinanceInfoSection(
        title = "자산 정보",
        step = FinancialInfoStep.ASSET,
        rows = listOf(
            FinanceInfoRow("총 보유 자산", formatManWon(totalAssetAmount)),
            FinanceInfoRow("금융 자산", formatManWon(cashSavings))
        )
    ),
    FinanceInfoSection(
        title = "부채 정보",
        step = FinancialInfoStep.DEBT,
        rows = listOf(
            FinanceInfoRow("총 부채 금액", formatManWon(totalDebtAmount)),
            FinanceInfoRow("월 상환액", formatManWon(monthlyDebtPaymentAmount))
        )
    ),
    FinanceInfoSection(
        title = "주택 보유 여부",
        step = FinancialInfoStep.HOUSE,
        rows = listOf(
            FinanceInfoRow(mapToHouseOption(housingOwnershipStatus.name) ?: NOT_ENTERED_LABEL)
        )
    )
)

/**
 * 금융 정보 프로필 조회가 실패했을 때(주로 아직 입력한 적 없는 계정, FINANCE404) 쓰는 빈 섹션.
 * [toFinanceInfoSections]와 같은 섹션/타이틀 구조를 유지하되 모든 값을 "없음"으로 채운다.
 */
fun emptyFinanceInfoSections(): List<FinanceInfoSection> = listOf(
    FinanceInfoSection(
        title = "소득 정보",
        step = FinancialInfoStep.INCOME,
        rows = listOf(FinanceInfoRow("연간 총소득", NOT_ENTERED_LABEL))
    ),
    FinanceInfoSection(
        title = "자산 정보",
        step = FinancialInfoStep.ASSET,
        rows = listOf(
            FinanceInfoRow("총 보유 자산", NOT_ENTERED_LABEL),
            FinanceInfoRow("금융 자산", NOT_ENTERED_LABEL)
        )
    ),
    FinanceInfoSection(
        title = "부채 정보",
        step = FinancialInfoStep.DEBT,
        rows = listOf(
            FinanceInfoRow("총 부채 금액", NOT_ENTERED_LABEL),
            FinanceInfoRow("월 상환액", NOT_ENTERED_LABEL)
        )
    ),
    FinanceInfoSection(
        title = "주택 보유 여부",
        step = FinancialInfoStep.HOUSE,
        rows = listOf(FinanceInfoRow(NOT_ENTERED_LABEL))
    )
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A4F55)
            )
            Text(
                text = "수정",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = EditLinkColor,
                modifier = Modifier.clickable(onClick = onEditClick)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CardBorderColor)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            section.rows.forEach { row ->
                if (row.value != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = row.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = LabelTextColor
                        )
                        Text(
                            text = row.value,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ValueTextColor
                        )
                    }
                } else {
                    Text(
                        text = row.label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = LabelTextColor
                    )
                }
            }
        }
    }
}
