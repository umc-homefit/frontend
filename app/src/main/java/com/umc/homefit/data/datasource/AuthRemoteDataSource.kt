package com.umc.homefit.data.datasource

import com.umc.homefit.data.dto.auth.LoginRequest
import com.umc.homefit.data.dto.auth.LoginResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun login(email: String, password: String): NetworkResult<LoginResponse> {
        return safeApiCall { authApiService.login(LoginRequest(email = email, password = password)) }
    }
}
