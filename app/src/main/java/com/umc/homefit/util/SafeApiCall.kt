package com.umc.homefit.util

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.common.ErrorResponse
import com.umc.homefit.util.error.ErrorCode
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

private val errorJson = Json { ignoreUnknownKeys = true }

suspend fun <T> safeApiCall(apiCall: suspend () -> BaseResponse<T>): NetworkResult<T> {
    return try {
        val response = apiCall()
        when {
            response.isSuccess && response.result != null -> NetworkResult.Success(response.result)
            response.isSuccess -> {
                @Suppress("UNCHECKED_CAST")
                NetworkResult.Success(Unit as T)
            }
            else -> NetworkResult.Error(ErrorCode.from(response.code), response.message)
        }
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val parsed = errorBody?.let {
            runCatching { errorJson.decodeFromString<ErrorResponse>(it) }.getOrNull()
        }
        NetworkResult.Error(
            errorCode = ErrorCode.from(parsed?.code ?: "UNKNOWN"),
            message = parsed?.message ?: "서버 오류가 발생했습니다"
        )
    } catch (e: IOException) {
        NetworkResult.Error(ErrorCode.UNKNOWN, "네트워크 연결을 확인해주세요")
    }
}
