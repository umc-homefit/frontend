package com.umc.homefit.data.dto.common

/**
 * 공고 상태. 백엔드 `status` 필드(RECRUITING/SCHEDULED/CLOSING_SOON/CLOSED)와 1:1 대응한다.
 * 한글 라벨은 담지 않는다 — 표시 텍스트는 각 API가 내려주는 statusDisplayText류 필드를 그대로 쓴다.
 */
enum class NoticeStatus {
    RECRUITING,
    SCHEDULED,
    CLOSING_SOON,
    CLOSED;

    companion object {
        fun fromApiValue(value: String?): NoticeStatus {
            return entries.find { it.name == value } ?: CLOSED
        }
    }
}
