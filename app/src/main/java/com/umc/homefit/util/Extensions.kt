package com.umc.homefit.util

import android.util.Log
import java.text.NumberFormat
import java.util.Locale

fun Any.logDebug(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Any.logError(message: String, throwable: Throwable? = null) {
    Log.e(this::class.java.simpleName, message, throwable)
}

private val KOREAN_NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.KOREA)

fun Long.toWonText(): String = "${KOREAN_NUMBER_FORMAT.format(this / 10_000)}만 원"

fun Int.toPercentileText(): String {
    val diff = 100 - this.coerceIn(0, 100)
    val lower = (diff / 10 * 10).coerceAtMost(90)
    val upper = (lower + 10).coerceAtMost(100)
    return "상위 $lower~$upper%"
}
