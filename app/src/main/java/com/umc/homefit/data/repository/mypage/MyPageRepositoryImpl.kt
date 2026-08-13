package com.umc.homefit.data.repository.mypage

import com.umc.homefit.data.api.mypage.MyPageApiService
import com.umc.homefit.data.dto.mypage.BasicInfoResponse
import com.umc.homefit.data.dto.mypage.ProfileResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.mypage.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApiService: MyPageApiService
) : MyPageRepository {

    override suspend fun getProfile(): NetworkResult<ProfileResponse> {
        return safeApiCall { myPageApiService.getProfile() }
    }

    override suspend fun getBasicInfo(): NetworkResult<BasicInfoResponse> {
        return safeApiCall { myPageApiService.getBasicInfo() }
    }
}
