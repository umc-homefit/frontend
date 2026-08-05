package com.umc.homefit.domain.repository.recruitment

import com.umc.homefit.data.dto.recruitment.SavedNoticeListResponse
import com.umc.homefit.data.dto.recruitment.UnsaveNoticeResultResponse
import com.umc.homefit.data.remote.NetworkResult

interface SavedNoticeRepository {
    suspend fun getSavedNotices(sort: String, page: Int, size: Int): NetworkResult<SavedNoticeListResponse>
    suspend fun unsaveNotice(noticeId: Long): NetworkResult<UnsaveNoticeResultResponse>
}
