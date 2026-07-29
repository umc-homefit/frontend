package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.dto.recruitment.RecruitmentStatus
import com.umc.homefit.presentation.theme.BackgroundLight
import com.umc.homefit.presentation.theme.RecruitmentAccent
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray
import com.umc.homefit.presentation.theme.SearchFieldBackground
import com.umc.homefit.presentation.theme.StatusClosingSoonBackground
import com.umc.homefit.presentation.theme.StatusClosingSoonText
import com.umc.homefit.presentation.theme.StatusRecruitingBackground
import com.umc.homefit.presentation.theme.StatusRecruitingText
import com.umc.homefit.presentation.theme.StatusScheduledBackground
import com.umc.homefit.presentation.theme.StatusScheduledText
import com.umc.homefit.presentation.theme.TextBlack

@Composable
fun RecruitmentTitleCard(recruitment: RecruitmentDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight)
            .border(BorderStroke(1.dp, SearchFieldBackground))
            .padding(start = 16.dp, end = 16.dp, top = 31.dp, bottom = 31.dp)
    ) {
        Text(
            text = recruitment.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatusChip(status = recruitment.status)
            recruitment.tags.forEach { tag ->
                TagChip(text = tag)
            }
        }
    }
}

@Composable
fun StatusChip(status: RecruitmentStatus) {
    val (background, text, label) = when (status) {
        RecruitmentStatus.RECRUITING -> Triple(StatusRecruitingBackground, StatusRecruitingText, "모집중")
        RecruitmentStatus.CLOSING_SOON -> Triple(StatusClosingSoonBackground, StatusClosingSoonText, "마감임박")
        RecruitmentStatus.SCHEDULED -> Triple(StatusScheduledBackground, StatusScheduledText, "예정")
    }
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(120.dp))
            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = text)
    }
}

@Composable
fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .border(BorderStroke(1.dp, RecruitmentBorder), RoundedCornerShape(120.dp))
            .padding(top = 5.dp, end = 12.dp, bottom = 5.dp, start = 12.dp)
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = RecruitmentTextGray)
    }
}

private val detailTabs = listOf("공고 상세", "경쟁률")

@Composable
fun RecruitmentTabRow(
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        detailTabs.forEachIndexed { index, label ->
            val selected = index == selectedTabIndex
            val barColor = if (selected) RecruitmentAccent else RecruitmentBorder
            val textColor = if (selected) TextBlack else RecruitmentBorder
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabClick(index) }
            ) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(barColor)
                )
            }
        }
    }
}
