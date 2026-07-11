package com.umc.homefit.util

import android.util.Log

fun Any.logDebug(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Any.logError(message: String, throwable: Throwable? = null) {
    Log.e(this::class.java.simpleName, message, throwable)
}
