package com.umc.homefit.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class RecruitmentSearchRepository @Inject constructor() {
    private val _recentSearches =
        MutableStateFlow(
            listOf(
                "청년안심주택",
                "천호동",
                "행복주택"
            )
        )

    val recentSearches: StateFlow<List<String>> =
        _recentSearches.asStateFlow()

    fun addRecentSearch(keyword: String) {
        val trimmedKeyword = keyword.trim()

        if (trimmedKeyword.isBlank()) return

        _recentSearches.update { currentKeywords ->
            listOf(trimmedKeyword) +
                currentKeywords.filterNot {
                    it == trimmedKeyword
                }
        }
    }

    fun deleteRecentSearch(keyword: String) {
        _recentSearches.update { currentKeywords ->
            currentKeywords.filterNot {
                it == keyword
            }
        }
    }
}
