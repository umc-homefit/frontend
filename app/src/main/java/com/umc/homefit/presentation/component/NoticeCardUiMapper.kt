package com.umc.homefit.presentation.component

import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.util.toWonText
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun NoticeDto.toNoticeCardUiModel(): NoticeCardUiModel {
    return NoticeCardUiModel(
        id = noticeId.toString(),
        title = title,
        infoLine1 = announcementNo?.let { "공고번호 | $it" },
        infoLine2 = buildAreaDepositLine(unitSummary, depositMin),
        infoLine3 = "청약접수 | ${applicationStartAt.toDisplayDate()} ~ ${applicationEndAt.toDisplayDate()}",
        status = NoticeStatus.fromApiValue(status),
        statusLabel = statusDisplayText,
        isSaved = isSaved
    )
}

private val DISPLAY_ZONE = ZoneId.of("Asia/Seoul")
private val DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd")

fun buildAreaDepositLine(unitSummary: String?, depositMin: Long?): String {
    val area = unitSummary?.removePrefix("전용")?.trim()?.takeIf { it.isNotBlank() } ?: "공고문 참고"
    return "전용 $area  보증금 ${depositMin?.toWonText() ?: "공고문 참고"}"
}

private fun String?.toDisplayDate(): String {
    if (this == null) return "공고문 참고"
    return runCatching { Instant.parse(this).atZone(DISPLAY_ZONE).format(DISPLAY_DATE_FORMATTER) }
        .getOrDefault("공고문 참고")
}
