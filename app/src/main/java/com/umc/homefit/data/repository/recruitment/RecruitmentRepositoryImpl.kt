package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.api.recruitment.NoticeApiService
import com.umc.homefit.data.dto.recruitment.NoticeDetailResponse
import com.umc.homefit.data.dto.recruitment.NoticeListResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import javax.inject.Inject

class RecruitmentRepositoryImpl @Inject constructor(
    private val noticeApiService: NoticeApiService
) : RecruitmentRepository {

    override suspend fun getRecruitments(
        keyword: String?,
        region: String?,
        district: String?,
        status: String?,
        isAdditionalRecruitment: Boolean?,
        minDeposit: Long?,
        maxDeposit: Long?,
        minArea: Double?,
        maxArea: Double?,
        sort: String?,
        page: Int,
        size: Int
    ): NetworkResult<NoticeListResponse> {
        return safeApiCall {
            noticeApiService.getNotices(
                keyword = keyword,
                region = region,
                district = district,
                status = status,
                isAdditionalRecruitment = isAdditionalRecruitment,
                minDeposit = minDeposit,
                maxDeposit = maxDeposit,
                minArea = minArea,
                maxArea = maxArea,
                sort = sort,
                page = page,
                size = size
            )
        }
    }

    override suspend fun getRecruitmentDetail(noticeId: Long): NetworkResult<NoticeDetailResponse> {
        return safeApiCall { noticeApiService.getNoticeDetail(noticeId) }
    }
}
