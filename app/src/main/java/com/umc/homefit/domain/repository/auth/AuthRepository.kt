package com.umc.homefit.domain.repository.auth

import com.umc.homefit.data.dto.auth.LoginResponse
import com.umc.homefit.data.remote.NetworkResult

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<LoginResponse>
    suspend fun signup(email: String, password: String): NetworkResult<LoginResponse>
}
