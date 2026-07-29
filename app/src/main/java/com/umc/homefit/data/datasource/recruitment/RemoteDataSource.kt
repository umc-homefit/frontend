package com.umc.homefit.data.datasource.recruitment

import com.umc.homefit.data.dto.recruitment.RecruitmentDto

interface RemoteDataSource {
    suspend fun getRecruitments(): List<RecruitmentDto>
    suspend fun getRecruitmentDetail(id: String): RecruitmentDto
}
