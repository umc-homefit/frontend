package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
import com.umc.homefit.ui.theme.BookmarkActive
import com.umc.homefit.ui.theme.BookmarkInactive
import com.umc.homefit.ui.theme.CompetitionRateBackground
import com.umc.homefit.ui.theme.CompetitionRateText
import com.umc.homefit.ui.theme.RecruitmentBorder
import com.umc.homefit.ui.theme.RecruitmentTextGray
import com.umc.homefit.ui.theme.StatusClosingSoonBackground
import com.umc.homefit.ui.theme.StatusClosingSoonText
import com.umc.homefit.ui.theme.StatusRecruitingBackground
import com.umc.homefit.ui.theme.StatusRecruitingText
import com.umc.homefit.ui.theme.StatusScheduledBackground
import com.umc.homefit.ui.theme.StatusScheduledText
import java.util.Locale

@Composable
fun RecruitmentCard(
    recruitment: RecruitmentDto,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RecruitmentBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recruitment.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (recruitment.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (recruitment.isBookmarked) "찜 해제" else "찜하기",
                    tint = if (recruitment.isBookmarked) BookmarkActive else BookmarkInactive,
                    modifier = Modifier.clickable(onClick = onToggleBookmark)
                )
            }

            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "공고번호 | ${recruitment.announcementNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecruitmentTextGray
                )
                Row {
                    Text(
                        text = "전용 | ${formatArea(recruitment.area)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RecruitmentTextGray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "보증금 | ${formatDepositToManwon(recruitment.depositMin)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RecruitmentTextGray
                    )
                }
                Text(
                    text = "청약접수 ${recruitment.applicationStartDate} ~ ${recruitment.applicationEndDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecruitmentTextGray
                )
            }

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusBadge(status = recruitment.status)
                CompetitionRateBadge(competitionRate = recruitment.competitionRate)
            }
        }
    }
}

@Composable
private fun StatusBadge(status: RecruitmentStatus) {
    val (background, text, label) = when (status) {
        RecruitmentStatus.RECRUITING -> Triple(StatusRecruitingBackground, StatusRecruitingText, "모집중")
        RecruitmentStatus.SCHEDULED -> Triple(StatusScheduledBackground, StatusScheduledText, "예정")
        RecruitmentStatus.CLOSING_SOON -> Triple(StatusClosingSoonBackground, StatusClosingSoonText, "마감임박")
    }
    Surface(color = background, shape = RoundedCornerShape(percent = 50)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = text,
            modifier = Modifier.padding(horizontal = 10.92.dp, vertical = 5.46.dp)
        )
    }
}

@Composable
private fun CompetitionRateBadge(competitionRate: String) {
    Surface(color = CompetitionRateBackground, shape = RoundedCornerShape(percent = 50)) {
        Text(
            text = "🔥경쟁률 $competitionRate",
            style = MaterialTheme.typography.labelSmall,
            color = CompetitionRateText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

private fun formatDepositToManwon(depositInWon: Long): String {
    return String.format(Locale.KOREA, "%,d만원", depositInWon / 10_000)
}

private fun formatArea(area: Double): String {
    return String.format(Locale.KOREA, "%.2f㎡", area)
}

@Preview(showBackground = true)
@Composable
fun RecruitmentCardPreview() {
    RecruitmentCard(
        recruitment = RecruitmentDto(
            id = "1",
            title = "2026년 행복주택 입주자 모집공고",
            company = "한국토지주택공사",
            location = "서울특별시 강남구",
            rentType = "월세",
            depositMin = 30000000,
            depositMax = 30000000,
            monthlyRentMin = 350000,
            monthlyRentMax = 350000,
            announcementDate = "2026-07-13",
            announcementNumber = "2026-강남-001",
            area = 39.87,
            applicationStartDate = "2026-07-14",
            applicationEndDate = "2026-07-18",
            status = RecruitmentStatus.RECRUITING,
            competitionRate = "12.3:1",
            isBookmarked = true
        ),
        onClick = {},
        onToggleBookmark = {}
    )
}
