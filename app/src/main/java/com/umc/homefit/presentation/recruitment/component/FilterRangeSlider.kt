@file:OptIn(ExperimentalMaterial3Api::class)

package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.presentation.theme.CompetitionRateText
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray

private val TrackThickness = 3.dp
private val ThumbSize = 23.dp
private val ThumbBorder = 1.dp
private val ThumbRadius = ThumbSize / 2
private val TickMarkThickness = 1.dp
private val TickMarkLength = 8.dp

data class FilterRangeSegment(
    val label: String,
    val range: ClosedFloatingPointRange<Float>
)
data class FilterRangeTick(val value: Float, val label: String)

@Composable
fun FilterRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    ticks: List<FilterRangeTick>,
    modifier: Modifier = Modifier,
    segments: List<FilterRangeSegment> = emptyList()
) {
    Column(modifier = modifier) {
        if (segments.isNotEmpty()) {
            FractionRow(modifier = Modifier.fillMaxWidth(), trackInset = ThumbRadius) {
                segments.forEach { segment ->
                    val isActive = value.start < segment.range.endInclusive && value.endInclusive > segment.range.start
                    val center = (segment.range.start + segment.range.endInclusive) / 2f
                    val position = fractionOf(center, valueRange)
                    Text(
                        text = segment.label,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isActive) CompetitionRateText else RecruitmentTextGray,
                        modifier = Modifier.fraction(position)
                    )
                }
            }
        }

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            RangeSlider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                startThumb = { FilterRangeThumb() },
                endThumb = { FilterRangeThumb() },
                track = { sliderState -> FilterRangeTrack(sliderState) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val trackWidth = maxWidth - (ThumbRadius * 2)
            ticks.forEach { tick ->
                val fraction = fractionOf(tick.value, valueRange)
                val xOffset = ThumbRadius + (trackWidth * fraction)
                Box(
                    modifier = Modifier
                        .offset(x = xOffset - (TickMarkThickness / 2))
                        .width(TickMarkThickness)
                        .height(TickMarkLength)
                        .background(RecruitmentTextGray)
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        FractionRow(modifier = Modifier.fillMaxWidth(), trackInset = ThumbRadius) {
            ticks.forEach { tick ->
                Text(
                    text = tick.label,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CompetitionRateText,
                    modifier = Modifier.fraction(fractionOf(tick.value, valueRange))
                )
            }
        }
    }
}

private fun fractionOf(value: Float, valueRange: ClosedFloatingPointRange<Float>): Float {
    val span = valueRange.endInclusive - valueRange.start
    return if (span == 0f) 0f else (value - valueRange.start) / span
}

@Composable
private fun FilterRangeThumb() {
    Box(
        modifier = Modifier
            .size(ThumbSize)
            .background(Color.White, CircleShape)
            .border(BorderStroke(ThumbBorder, CompetitionRateText), CircleShape)
    )
}

@Composable
private fun FilterRangeTrack(sliderState: RangeSliderState) {
    val valueRange = sliderState.valueRange
    val startFraction = fractionOf(sliderState.activeRangeStart, valueRange)
    val endFraction = fractionOf(sliderState.activeRangeEnd, valueRange)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(ThumbSize)
    ) {
        val centerY = size.height / 2f
        val strokeWidthPx = TrackThickness.toPx()
        drawLine(
            color = RecruitmentBorder,
            start = Offset(0f, centerY),
            end = Offset(size.width, centerY),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round
        )
        drawLine(
            color = CompetitionRateText,
            start = Offset(size.width * startFraction, centerY),
            end = Offset(size.width * endFraction, centerY),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round
        )
    }
}

private data class FractionParentData(val fraction: Float)

private class FractionParentDataModifier(
    private val fraction: Float
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = FractionParentData(fraction)
}

private fun Modifier.fraction(fraction: Float): Modifier =
    this.then(FractionParentDataModifier(fraction))

// trackInset must match ThumbRadius so labels center on the same track range as the slider/tick marks
@Composable
private fun FractionRow(
    modifier: Modifier = Modifier,
    trackInset: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(looseConstraints) }
        val width = constraints.maxWidth
        val insetPx = trackInset.roundToPx()
        val trackWidth = (width - insetPx * 2).coerceAtLeast(0)
        val height = placeables.maxOfOrNull { it.height } ?: 0

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val data = measurables[index].parentData as? FractionParentData
                    ?: FractionParentData(0f)
                val centerX = insetPx + trackWidth * data.fraction
                val rawX = centerX - placeable.width / 2f
                val x = rawX.toInt().coerceIn(0, (width - placeable.width).coerceAtLeast(0))
                placeable.placeRelative(x, 0)
            }
        }
    }
}
