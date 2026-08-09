package com.umc.homefit.util

import android.util.Log

fun Any.logDebug(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Any.logError(message: String, throwable: Throwable? = null) {
    Log.e(this::class.java.simpleName, message, throwable)
}

/** 원 단위 금액을 "1,000만 원" 형태로 변환 (예: 10_000_000 -> "1,000만 원") */
fun Long.toWonText(): String {
    val manValue = Math.round(this / 10_000.0)
    return "%,d만 원".format(manValue)
}

/**
 * eligibilityScore(0~100)를 10점 단위 구간으로 나눠 "상위 N0~M0%" 텍스트로 변환
 * 점수가 높을수록 상위 %가 낮아지도록 반전 (예: 91~100점 -> "상위 0~10%", 0~10점 -> "상위 90~100%")
 */
fun Int.toPercentileText(): String {
    val diff = 100 - this.coerceIn(0, 100)
    val lower = (diff / 10 * 10).coerceAtMost(90)
    val upper = (lower + 10).coerceAtMost(100)
    return "상위 $lower~$upper%"
}
