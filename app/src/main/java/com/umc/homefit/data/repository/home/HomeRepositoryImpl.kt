package com.umc.homefit.data.repository.home

import com.umc.homefit.data.api.recruitment.NoticeApiService
import com.umc.homefit.data.dto.recruitment.NoticeListResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.home.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val noticeApiService: NoticeApiService
) : HomeRepository {

    override suspend fun getNotices(
        status: String,
        sort: String,
        page: Int,
        size: Int
    ): NetworkResult<NoticeListResponse> =
        safeApiCall {
            noticeApiService.getNotices(
                status = status,
                sort = sort,
                page = page,
                size = size
            )
        }
}
