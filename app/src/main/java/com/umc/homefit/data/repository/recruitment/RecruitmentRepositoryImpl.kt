package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.api.recruitment.NoticeApiService
import com.umc.homefit.data.dto.recruitment.NoticeListResponse
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.mock.RecruitmentMockData
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
        // Mock 데이터 (백엔드 연동 전 사용, 롤백 대비 보존)
        // return NetworkResult.Success(RecruitmentMockData.getRecruitments())
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

    override suspend fun getRecruitmentDetail(id: String): RecruitmentDto {
        return RecruitmentMockData.getRecruitmentDetail(id)
    }
}
