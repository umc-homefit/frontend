package com.umc.homefit.domain.repository.finance

import com.umc.homefit.data.local.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductSearchRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    val recentSearches: Flow<List<String>> = userPreferencesDataSource.productRecentSearches

    suspend fun addRecentSearch(keyword: String) {
        val trimmedKeyword = keyword.trim()

        if (trimmedKeyword.isBlank()) return

        userPreferencesDataSource.addProductRecentSearch(trimmedKeyword)
    }

    suspend fun deleteRecentSearch(keyword: String) {
        userPreferencesDataSource.removeProductRecentSearch(keyword)
    }
}
