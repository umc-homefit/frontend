package com.umc.homefit.presentation.component

import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.data.dto.recruitment.NoticeDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * [NoticeDto] → [NoticeCardUiModel] 매핑. 공고 목록/홈 화면 "주요 공고"가 동일한 DTO를 쓰므로 공유한다.
 *
 * dDayText/competitionRate는 일부러 안 채운다 — 상태 뱃지 옆 자리는 경쟁률(competitionRate) 뱃지 자리인데,
 * 백엔드가 NoticeDto에 아직 경쟁률 데이터를 안 내려줘서 비워둔 상태다. D-Day를 대신 채우면 안 된다.
 */
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

private fun buildAreaDepositLine(unitSummary: String?, depositMin: Long?): String {
    val area = unitSummary?.removePrefix("전용")?.trim()?.takeIf { it.isNotBlank() } ?: "공고문 참고"
    return "전용 $area  보증금 ${formatDepositToManwon(depositMin)}"
}

private fun formatDepositToManwon(depositInWon: Long?): String {
    if (depositInWon == null) return "공고문 참고"
    return String.format(Locale.KOREA, "%,d만원", depositInWon / 10_000)
}

private fun String?.toDisplayDate(): String {
    if (this == null) return "공고문 참고"
    return runCatching { Instant.parse(this).atZone(DISPLAY_ZONE).format(DISPLAY_DATE_FORMATTER) }
        .getOrDefault("공고문 참고")
}
