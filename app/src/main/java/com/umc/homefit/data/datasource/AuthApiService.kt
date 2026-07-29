package com.umc.homefit.data.datasource

import com.umc.homefit.data.dto.auth.LoginRequest
import com.umc.homefit.data.dto.auth.LoginResponse
import com.umc.homefit.data.dto.common.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

}
