package com.umc.homefit.domain.repository.home

import com.umc.homefit.data.dto.recruitment.NoticeListResponse
import com.umc.homefit.data.remote.NetworkResult

interface HomeRepository {
    suspend fun getNotices(
        status: String,
        sort: String,
        page: Int,
        size: Int
    ): NetworkResult<NoticeListResponse>
}
