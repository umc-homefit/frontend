package com.umc.homefit.data.repository

import com.umc.homefit.data.dto.RecruitmentDto

interface RecruitmentRepository {
    suspend fun getRecruitments(): List<RecruitmentDto>
    suspend fun getRecruitmentDetail(id: String): RecruitmentDto
}
