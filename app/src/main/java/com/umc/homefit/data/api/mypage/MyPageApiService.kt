package com.umc.homefit.data.api.mypage

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.mypage.BasicInfoResponse
import com.umc.homefit.data.dto.mypage.ProfileResponse
import retrofit2.http.GET

interface MyPageApiService {

    @GET("users/me/profile")
    suspend fun getProfile(): BaseResponse<ProfileResponse>

    @GET("users/me")
    suspend fun getBasicInfo(): BaseResponse<BasicInfoResponse>
}
