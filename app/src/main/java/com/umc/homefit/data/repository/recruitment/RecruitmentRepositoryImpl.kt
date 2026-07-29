package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.mock.RecruitmentMockData
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import javax.inject.Inject

class RecruitmentRepositoryImpl @Inject constructor() : RecruitmentRepository {

    override suspend fun getRecruitments(): List<RecruitmentDto> {
        return RecruitmentMockData.getRecruitments()
    }

    override suspend fun getRecruitmentDetail(id: String): RecruitmentDto {
        return RecruitmentMockData.getRecruitmentDetail(id)
    }
}
