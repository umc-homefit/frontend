package com.umc.homefit.domain.repository.recruitment

import com.umc.homefit.data.dto.recruitment.NoticeDetailResponse
import com.umc.homefit.data.remote.NetworkResult

interface NoticeDetailRepository {
    suspend fun getNoticeDetail(noticeId: Long): NetworkResult<NoticeDetailResponse>
}
