package com.umc.homefit.domain.repository.recruitment

import com.umc.homefit.data.dto.recruitment.NoticeListResponse
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.remote.NetworkResult

interface RecruitmentRepository {
    suspend fun getRecruitments(
        keyword: String? = null,
        region: String? = null,
        district: String? = null,
        status: String? = null,
        isAdditionalRecruitment: Boolean? = null,
        minDeposit: Long? = null,
        maxDeposit: Long? = null,
        minArea: Double? = null,
        maxArea: Double? = null,
        sort: String? = null,
        page: Int = 0,
        size: Int = 50
    ): NetworkResult<NoticeListResponse>

    suspend fun getRecruitmentDetail(id: String): RecruitmentDto
}
