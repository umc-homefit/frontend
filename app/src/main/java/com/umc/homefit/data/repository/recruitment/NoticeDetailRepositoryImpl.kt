package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.api.recruitment.NoticeApiService
import com.umc.homefit.data.dto.recruitment.NoticeDetailResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.recruitment.NoticeDetailRepository
import javax.inject.Inject

class NoticeDetailRepositoryImpl @Inject constructor(
    private val noticeApiService: NoticeApiService
) : NoticeDetailRepository {

    override suspend fun getNoticeDetail(noticeId: Long): NetworkResult<NoticeDetailResponse> {
        return safeApiCall { noticeApiService.getNoticeDetail(noticeId) }
    }
}
