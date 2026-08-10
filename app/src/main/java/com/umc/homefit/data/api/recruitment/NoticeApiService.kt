package com.umc.homefit.data.api.recruitment

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.recruitment.SavedNoticeListResponse
import com.umc.homefit.data.dto.recruitment.UnsaveNoticeResultResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeApiService {

    @GET("users/me/saved-notices")
    suspend fun getSavedNotices(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<SavedNoticeListResponse>

    @DELETE("notices/{noticeId}/save")
    suspend fun unsaveNotice(@Path("noticeId") noticeId: Long): BaseResponse<UnsaveNoticeResultResponse>
}
