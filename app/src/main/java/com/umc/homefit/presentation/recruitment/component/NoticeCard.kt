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

// RecruitmentCard(component/RecruitmentCard.kt)는 HomeScreen과 공유 중이라 시그니처를 바꿀 수 없어
// GET /notices 응답 전용으로 별도 카드를 둠. RecruitmentDto가 아니라 NoticeDto를 그린다.

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
                verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${notice.region} ${notice.district}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecruitmentTextGray
                )
                Row {
                    Text(
                        text = "전용 | ${notice.unitSummary}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RecruitmentTextGray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "보증금 | ${formatDepositToManwon(notice.depositMin)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RecruitmentTextGray
                    )
                }
                Text(
                    text = "청약접수 ${notice.applicationStartAt.toDisplayDate()} ~ ${notice.applicationEndAt.toDisplayDate()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecruitmentTextGray
                )
            }

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NoticeStatusBadge(status = notice.status, label = notice.statusDisplayText)
                notice.dDayText?.let { DDayBadge(text = it) }
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
        // TODO: 디자이너 확인 후 CLOSED 전용 색상 토큰으로 교체 (현재는 SCHEDULED와 동일한 중립 톤으로 대체)
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

@Composable
private fun DDayBadge(text: String) {
    Surface(color = StatusClosingSoonBackground, shape = RoundedCornerShape(percent = 50)) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = StatusClosingSoonText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

private fun formatDepositToManwon(depositInWon: Long): String {
    return String.format(Locale.KOREA, "%,d만원", depositInWon / 10_000)
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
            title = "강동구 청년안심주택 추가모집",
            region = "서울",
            district = "강동구",
            unitSummary = "전용 24㎡",
            depositMin = 32000000,
            depositMax = 48000000,
            monthlyRentMin = 280000,
            monthlyRentMax = 410000,
            status = "RECRUITING",
            statusDisplayText = "모집중",
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
