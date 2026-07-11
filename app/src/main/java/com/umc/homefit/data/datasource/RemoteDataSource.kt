package com.umc.homefit.data.datasource

import com.umc.homefit.data.dto.RecruitmentDto

interface RemoteDataSource {
    suspend fun getRecruitments(): List<RecruitmentDto>
    suspend fun getRecruitmentDetail(id: String): RecruitmentDto
}
