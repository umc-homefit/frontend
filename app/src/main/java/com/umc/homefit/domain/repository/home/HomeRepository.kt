package com.umc.homefit.domain.repository.home

import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun fetchFeaturedRecruitments(): List<RecruitmentDto>
    fun getSavedRecruitmentIds(): Flow<List<String>>
    suspend fun saveRecruitmentId(id: String)
}
