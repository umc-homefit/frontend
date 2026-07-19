package com.umc.homefit.data.repository

import com.umc.homefit.data.datasource.local.UserPreferencesDataSource
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus
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
                depositMin = 50000000L,
                depositMax = 50000000L,
                monthlyRentMin = 150000L,
                monthlyRentMax = 150000L,
                announcementDate = "2026-07-11",
                announcementNumber = "2026-마포-003",
                area = 39.87,
                applicationStartDate = "2026-07-12",
                applicationEndDate = "2026-07-16",
                status = RecruitmentStatus.RECRUITING,
                competitionRate = "8.2:1"
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
