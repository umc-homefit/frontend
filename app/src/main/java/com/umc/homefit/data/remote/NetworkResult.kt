package com.umc.homefit.data.remote

import com.umc.homefit.util.error.ErrorCode

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val errorCode: ErrorCode, val message: String) : NetworkResult<Nothing>()
}
