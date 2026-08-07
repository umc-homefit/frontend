package com.umc.homefit.data.api.recruitment

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.recruitment.NoticeListResponse
import com.umc.homefit.data.dto.recruitment.SavedNoticeListResponse
import com.umc.homefit.data.dto.recruitment.UnsaveNoticeResultResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeApiService {

    @GET("notices")
    suspend fun getNotices(
        @Query("keyword") keyword: String? = null,
        @Query("region") region: String? = null,
        @Query("district") district: String? = null,
        @Query("status") status: String? = null,
        @Query("isAdditionalRecruitment") isAdditionalRecruitment: Boolean? = null,
        @Query("minDeposit") minDeposit: Long? = null,
        @Query("maxDeposit") maxDeposit: Long? = null,
        @Query("minArea") minArea: Double? = null,
        @Query("maxArea") maxArea: Double? = null,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): BaseResponse<NoticeListResponse>

    @GET("users/me/saved-notices")
    suspend fun getSavedNotices(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<SavedNoticeListResponse>

    @DELETE("notices/{noticeId}/save")
    suspend fun unsaveNotice(@Path("noticeId") noticeId: Long): BaseResponse<UnsaveNoticeResultResponse>
}
