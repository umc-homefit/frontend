package com.umc.homefit.domain.repository.mypage

import com.umc.homefit.data.dto.mypage.BasicInfoResponse
import com.umc.homefit.data.dto.mypage.ProfileResponse
import com.umc.homefit.data.remote.NetworkResult

interface MyPageRepository {
    suspend fun getProfile(): NetworkResult<ProfileResponse>
    suspend fun getBasicInfo(): NetworkResult<BasicInfoResponse>
}
