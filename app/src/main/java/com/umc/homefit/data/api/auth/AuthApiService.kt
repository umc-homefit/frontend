package com.umc.homefit.data.api.auth

import com.umc.homefit.data.dto.auth.LoginRequest
import com.umc.homefit.data.dto.auth.LoginResponse
import com.umc.homefit.data.dto.auth.SignupRequest
import com.umc.homefit.data.dto.auth.SocialAuthRequest
import com.umc.homefit.data.dto.common.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): BaseResponse<LoginResponse>

    @POST("auth/social")
    suspend fun socialLogin(@Body request: SocialAuthRequest): BaseResponse<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): BaseResponse<Unit>

}


