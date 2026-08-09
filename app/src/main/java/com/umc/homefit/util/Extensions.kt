package com.umc.homefit.util

import android.util.Log

fun Any.logDebug(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Any.logError(message: String, throwable: Throwable? = null) {
    Log.e(this::class.java.simpleName, message, throwable)
}

fun Long.toWonText(): String {
    val manValue = Math.round(this / 10_000.0)
    return "%,d만 원".format(manValue)
}

fun Int.toPercentileText(): String {
    val diff = 100 - this.coerceIn(0, 100)
    val lower = (diff / 10 * 10).coerceAtMost(90)
    val upper = (lower + 10).coerceAtMost(100)
    return "상위 $lower~$upper%"
}
