package com.umc.homefit.domain.repository.recruitment

import com.umc.homefit.data.dto.recruitment.RecruitmentDto

interface RecruitmentRepository {
    suspend fun getRecruitments(): List<RecruitmentDto>
    suspend fun getRecruitmentDetail(id: String): RecruitmentDto
}
