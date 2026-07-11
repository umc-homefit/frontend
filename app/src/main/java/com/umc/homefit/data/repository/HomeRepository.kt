package com.umc.homefit.data.repository

import com.umc.homefit.data.dto.RecruitmentDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun fetchFeaturedRecruitments(): List<RecruitmentDto>
    fun getSavedRecruitmentIds(): Flow<List<String>>
    suspend fun saveRecruitmentId(id: String)
}
