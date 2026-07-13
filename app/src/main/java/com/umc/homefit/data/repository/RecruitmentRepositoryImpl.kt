package com.umc.homefit.data.repository

import com.umc.homefit.data.datasource.RemoteDataSource
import com.umc.homefit.data.dto.RecruitmentDto
import javax.inject.Inject

class RecruitmentRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : RecruitmentRepository {

    override suspend fun getRecruitments(): List<RecruitmentDto> {
        return remoteDataSource.getRecruitments()
    }

    override suspend fun getRecruitmentDetail(id: String): RecruitmentDto {
        return remoteDataSource.getRecruitmentDetail(id)
    }
}
