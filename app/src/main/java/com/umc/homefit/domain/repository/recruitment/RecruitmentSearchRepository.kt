package com.umc.homefit.domain.repository.recruitment

import com.umc.homefit.data.local.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecruitmentSearchRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    val recentSearches: Flow<List<String>> = userPreferencesDataSource.recruitmentRecentSearches

    suspend fun addRecentSearch(keyword: String) {
        val trimmedKeyword = keyword.trim()

        if (trimmedKeyword.isBlank()) return

        userPreferencesDataSource.addRecruitmentRecentSearch(trimmedKeyword)
    }

    suspend fun deleteRecentSearch(keyword: String) {
        userPreferencesDataSource.removeRecruitmentRecentSearch(keyword)
    }
}
