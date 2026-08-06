package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.api.recruitment.NoticeApiService
import com.umc.homefit.data.dto.recruitment.SavedNoticeListResponse
import com.umc.homefit.data.dto.recruitment.UnsaveNoticeResultResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import javax.inject.Inject

class SavedNoticeRepositoryImpl @Inject constructor(
    private val noticeApiService: NoticeApiService
) : SavedNoticeRepository {

    override suspend fun getSavedNotices(sort: String, page: Int, size: Int): NetworkResult<SavedNoticeListResponse> {
        return safeApiCall { noticeApiService.getSavedNotices(sort, page, size) }
    }

    override suspend fun unsaveNotice(noticeId: Long): NetworkResult<UnsaveNoticeResultResponse> {
        return safeApiCall { noticeApiService.unsaveNotice(noticeId) }
    }
}
