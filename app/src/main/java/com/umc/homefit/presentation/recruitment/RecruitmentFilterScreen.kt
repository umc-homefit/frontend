package com.umc.homefit.presentation.recruitment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.presentation.recruitment.component.DistrictDropdownField
import com.umc.homefit.presentation.recruitment.component.FilterRangeSegment
import com.umc.homefit.presentation.recruitment.component.FilterRangeSlider
import com.umc.homefit.presentation.recruitment.component.FilterRangeTick
import com.umc.homefit.ui.component.AppScaffold
import com.umc.homefit.ui.component.TopBarAction
import com.umc.homefit.ui.theme.RecruitmentAccent
import com.umc.homefit.ui.theme.RecruitmentTextGray
import com.umc.homefit.ui.theme.SearchFieldBackground
import kotlin.math.roundToInt

// TitleColor는 Color.kt에 대응 토큰이 아직 없어 로컬로 유지
private val TitleColor = Color(0xFF161616)

private val AreaSegments = listOf(
    FilterRangeSegment("원룸", 0f..20f),
    FilterRangeSegment("1.5룸", 20f..40f),
    FilterRangeSegment("2룸", 40f..59f)
)

private val AreaTicks = listOf(
    FilterRangeTick(0f, "0m²"),
    FilterRangeTick(20f, "20m²"),
    FilterRangeTick(40f, "40m²"),
    FilterRangeTick(59f, "59m²")
)

private val DepositTicks = listOf(
    FilterRangeTick(0f, "0"),
    FilterRangeTick(2500f, "2.5천"),
    FilterRangeTick(5000f, "5천"),
    FilterRangeTick(7500f, "7.5천"),
    FilterRangeTick(10000f, "1억")
)

@Composable
fun RecruitmentFilterScreenRoute(
    viewModel: RecruitmentFilterScreenViewModel,
    onApply: (FilterState) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val districts by viewModel.districts.collectAsState()

    RecruitmentFilterScreen(
        uiState = uiState,
        districts = districts,
        onDistrictSelected = viewModel::updateDistrict,
        onAreaChange = viewModel::updateArea,
        onDepositChange = viewModel::updateDeposit,
        onReset = viewModel::resetFilter,
        onApply = onApply,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun RecruitmentFilterScreen(
    uiState: RecruitmentFilterScreenUiState,
    districts: List<String>,
    onDistrictSelected: (String) -> Unit,
    onAreaChange: (Float, Float) -> Unit,
    onDepositChange: (Float, Float) -> Unit,
    onReset: () -> Unit,
    onApply: (FilterState) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = "공고 필터링",
        showBackButton = false,
        actions = listOf(
            TopBarAction(
                icon = painterResource(id = R.drawable.ic_filter_close),
                contentDescription = "닫기",
                onClick = onBack
            )
        ),
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            when (uiState) {
                is RecruitmentFilterScreenUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is RecruitmentFilterScreenUiState.Success -> {
                    RecruitmentFilterContent(
                        filterState = uiState.data,
                        districts = districts,
                        onDistrictSelected = onDistrictSelected,
                        onAreaChange = onAreaChange,
                        onDepositChange = onDepositChange,
                        onReset = onReset,
                        onApply = onApply,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 60.dp)
                    )
                }
                is RecruitmentFilterScreenUiState.Error -> {
                    Text(
                        text = "Error: ${uiState.message}",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecruitmentFilterContent(
    filterState: FilterState,
    districts: List<String>,
    onDistrictSelected: (String) -> Unit,
    onAreaChange: (Float, Float) -> Unit,
    onDepositChange: (Float, Float) -> Unit,
    onReset: () -> Unit,
    onApply: (FilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "지역",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TitleColor
            )
            Spacer(modifier = Modifier.height(10.99.dp))
            DistrictDropdownField(
                districts = districts,
                selectedDistrict = filterState.selectedDistrict,
                onDistrictSelected = onDistrictSelected,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(53.13.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "전용 면적",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TitleColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formatAreaLabel(filterState.minArea, filterState.maxArea),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RecruitmentAccent,
                    textAlign = TextAlign.Right
                )
            }
            Spacer(modifier = Modifier.height(25.01.dp))
            FilterRangeSlider(
                value = filterState.minArea..filterState.maxArea,
                onValueChange = { range -> onAreaChange(range.start, range.endInclusive) },
                valueRange = 0f..59f,
                segments = AreaSegments,
                ticks = AreaTicks,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(53.02.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "보증금",
                    fontSize = 16.00.sp,
                    fontWeight = FontWeight.Bold,
                    color = TitleColor
                )
                Spacer(modifier = Modifier.width(3.66.dp))
                Text(
                    text = "(만 원)",
                    fontSize = 12.00.sp,
                    fontWeight = FontWeight.Medium,
                    color = RecruitmentTextGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formatDepositLabel(filterState.minDeposit, filterState.maxDeposit),
                    fontSize = 16.00.sp,
                    fontWeight = FontWeight.Medium,
                    color = RecruitmentAccent,
                    textAlign = TextAlign.Right
                )
            }
            Spacer(modifier = Modifier.height(25.01.dp))
            FilterRangeSlider(
                value = filterState.minDeposit..filterState.maxDeposit,
                onValueChange = { range -> onDepositChange(range.start, range.endInclusive) },
                valueRange = 0f..10000f,
                ticks = DepositTicks,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 21.98.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(4.00.dp))
                    .background(SearchFieldBackground)
                    .clickable(onClick = onReset),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "필터 초기화",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RecruitmentTextGray,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(4.00.dp))
                    .background(Brush.horizontalGradient(colors = listOf(RecruitmentAccent, RecruitmentAccent.copy(alpha = 0.5f))))
                    .clickable { onApply(filterState) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "적용하기",
                    fontSize = 16.00.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatAreaLabel(minArea: Float, maxArea: Float): String {
    return "${minArea.roundToInt()}~${maxArea.roundToInt()}㎡"
}

private fun formatDepositLabel(minDeposit: Float, maxDeposit: Float): String {
    return "${formatDepositValue(minDeposit)}~${formatDepositValue(maxDeposit)}"
}

private fun formatDepositValue(value: Float): String {
    return when {
        value >= 10000f -> "1억"
        value >= 1000f -> {
            val thousands = value / 1000f
            if (thousands == thousands.toInt().toFloat()) {
                "${thousands.toInt()}천"
            } else {
                "${"%.1f".format(thousands)}천"
            }
        }
        else -> "${value.toInt()}"
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentFilterScreenPreview() {
    RecruitmentFilterScreen(
        uiState = RecruitmentFilterScreenUiState.Success(
            FilterState(maxArea = 20f, maxDeposit = 7500f)
        ),
        districts = listOf("전체", "강남구", "강동구", "서초구"),
        onDistrictSelected = {},
        onAreaChange = { _, _ -> },
        onDepositChange = { _, _ -> },
        onReset = {},
        onApply = {},
        onBack = {}
    )
}
