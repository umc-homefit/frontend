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
