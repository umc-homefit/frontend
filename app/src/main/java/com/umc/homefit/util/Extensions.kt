package com.umc.homefit.util

import android.util.Log
import java.text.NumberFormat
import java.util.Locale

fun Any.logError(message: String, throwable: Throwable? = null) {
    Log.e(this::class.java.simpleName, message, throwable)
}

private val KOREAN_NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.KOREA)

fun Long.toWonText(): String {
    val manwon = this / 10_000
    val eok = manwon / 10_000
    val remainingManwon = manwon % 10_000
    return when {
        eok == 0L -> "${KOREAN_NUMBER_FORMAT.format(manwon)}만 원"
        remainingManwon == 0L -> "${eok}억 원"
        else -> "${eok}억 ${KOREAN_NUMBER_FORMAT.format(remainingManwon)}만 원"
    }
}

fun Double.toAreaText(): String =
    if (this % 1.0 == 0.0) "${toInt()}" else "$this"

fun Int.toPercentileText(): String {
    val diff = 100 - this.coerceIn(0, 100)
    val lower = (diff / 10 * 10).coerceAtMost(90)
    val upper = (lower + 10).coerceAtMost(100)
    return "상위 $lower~$upper%"
}
