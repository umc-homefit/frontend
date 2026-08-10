package com.umc.homefit.presentation.recruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.presentation.theme.BookmarkActive
import com.umc.homefit.presentation.theme.BookmarkInactive
import com.umc.homefit.presentation.theme.RecruitmentBorder
import com.umc.homefit.presentation.theme.RecruitmentTextGray
import com.umc.homefit.presentation.theme.StatusClosingSoonBackground
import com.umc.homefit.presentation.theme.StatusClosingSoonText
import com.umc.homefit.presentation.theme.StatusRecruitingBackground
import com.umc.homefit.presentation.theme.StatusRecruitingText
import com.umc.homefit.presentation.theme.StatusScheduledBackground
import com.umc.homefit.presentation.theme.StatusScheduledText
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


private val DISPLAY_ZONE = ZoneId.of("Asia/Seoul")
private val DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd")

@Composable
fun NoticeCard(
    notice: NoticeDto,
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
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (notice.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (notice.isSaved) "찜 해제" else "찜하기",
                    tint = if (notice.isSaved) BookmarkActive else BookmarkInactive,
                    modifier = Modifier.clickable(onClick = onToggleBookmark)
                )
            }

            Column(
                modifier = Modifier.padding(top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                notice.announcementNo?.let { announcementNo ->
                    Text(
                        text = "공고번호 | $announcementNo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RecruitmentTextGray
                    )
                }
                Text(
                    text = "${notice.unitSummary.toAreaText()}   보증금 | ${formatDepositToManwon(notice.depositMin)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecruitmentTextGray
                )
                Text(
                    text = "청약접수 | ${notice.applicationStartAt.toDisplayDate()} ~ ${notice.applicationEndAt.toDisplayDate()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecruitmentTextGray
                )
            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NoticeStatusBadge(status = notice.status, label = notice.statusDisplayText)
            }
        }
    }
}

@Composable
private fun NoticeStatusBadge(status: String, label: String) {
    val (background, text) = when (status) {
        "RECRUITING" -> StatusRecruitingBackground to StatusRecruitingText
        "SCHEDULED" -> StatusScheduledBackground to StatusScheduledText
        "CLOSING_SOON" -> StatusClosingSoonBackground to StatusClosingSoonText
        else -> StatusScheduledBackground to StatusScheduledText
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

private fun formatDepositToManwon(depositInWon: Long?): String {
    if (depositInWon == null) return "공고문 참고"
    return String.format(Locale.KOREA, "%,d만원", depositInWon / 10_000)
}

private fun String?.toAreaText(): String {
    if (isNullOrBlank()) return "전용 | 공고문 참고"
    val area = removePrefix("전용").trim()
    return "전용 | $area"
}

private fun String?.toDisplayDate(): String {
    if (this == null) return "공고문 참고"
    return runCatching { Instant.parse(this).atZone(DISPLAY_ZONE).format(DISPLAY_DATE_FORMATTER) }
        .getOrDefault("공고문 참고")
}

@Preview(showBackground = true)
@Composable
private fun NoticeCardPreview() {
    NoticeCard(
        notice = NoticeDto(
            noticeId = 1,
            title = "강동구 청년안심주택 추가모집 · 모집중",
            announcementNo = "2026-강동-003",
            region = "서울",
            district = "강동구",
            unitSummary = "전용 24㎡",
            depositMin = 32000000,
            depositMax = 48000000,
            monthlyRentMin = 280000,
            monthlyRentMax = 410000,
            status = "CLOSING_SOON",
            statusDisplayText = "마감임박",
            isAdditionalRecruitment = true,
            applicationStartAt = "2026-07-01T10:00:00+09:00",
            applicationEndAt = "2026-07-10T18:00:00+09:00",
            dDayText = "D-3",
            views = 120,
            interestedCount = 32,
            isSaved = false
        ),
        onClick = {},
        onToggleBookmark = {}
    )
}
