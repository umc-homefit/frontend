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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.R
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
import com.umc.homefit.ui.theme.BackgroundLight
import com.umc.homefit.ui.theme.BookmarkActive
import com.umc.homefit.ui.theme.BookmarkInactive
import com.umc.homefit.ui.theme.RecruitmentAccent
import com.umc.homefit.ui.theme.RecruitmentBorder
import com.umc.homefit.ui.theme.RecruitmentTextGray
import com.umc.homefit.ui.theme.SearchFieldBackground
import com.umc.homefit.ui.theme.StatusClosingSoonBackground
import com.umc.homefit.ui.theme.StatusClosingSoonText
import com.umc.homefit.ui.theme.StatusRecruitingBackground
import com.umc.homefit.ui.theme.StatusRecruitingText
import com.umc.homefit.ui.theme.StatusScheduledBackground
import com.umc.homefit.ui.theme.StatusScheduledText
import com.umc.homefit.ui.theme.TextBlack

@Composable
fun RecruitmentTopBar(
    isBookmarked: Boolean,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(BackgroundLight)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 12.dp)
                    .size(24.dp)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_top_arrow_back),
                    contentDescription = "뒤로가기",
                    tint = RecruitmentTextGray,
                    modifier = Modifier
                        .width(7.67.dp)
                        .height(13.31.dp)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_top_save),
                    contentDescription = if (isBookmarked) "찜 해제" else "찜하기",
                    tint = if (isBookmarked) BookmarkActive else BookmarkInactive,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onBookmarkClick)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_top_share),
                    contentDescription = "공유",
                    tint = BookmarkInactive,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SearchFieldBackground)
        )
    }
}

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
