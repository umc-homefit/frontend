package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.datasource.recruitment.RemoteDataSource
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
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
