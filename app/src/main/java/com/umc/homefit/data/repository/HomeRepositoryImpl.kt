package com.umc.homefit.data.repository

import com.umc.homefit.data.datasource.local.UserPreferencesDataSource
import com.umc.homefit.data.dto.RecruitmentDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : HomeRepository {

    override suspend fun fetchFeaturedRecruitments(): List<RecruitmentDto> {
        return listOf(
            RecruitmentDto(
                id = "1",
                title = "행복주택 서울가좌역",
                company = "LH한국토지주택공사",
                location = "서울특별시 마포구",
                rentType = "행복주택",
                deposit = 50000000L,
                monthlyRent = 150000L,
                announcementDate = "2026-07-11"
            )
        )
    }

    override fun getSavedRecruitmentIds(): Flow<List<String>> {
        return userPreferencesDataSource.savedRecruitmentIds
    }

    override suspend fun saveRecruitmentId(id: String) {
        userPreferencesDataSource.saveRecruitmentId(id)
    }
}
