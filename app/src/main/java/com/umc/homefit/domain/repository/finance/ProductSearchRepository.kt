package com.umc.homefit.domain.repository.finance

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductSearchRepository @Inject constructor() {

    private val _recentSearches =
        MutableStateFlow(
            listOf(
                "디딤돌 대출",
                "주택청약종합저축",
                "국민은행"
            )
        )

    val recentSearches =
        _recentSearches.asStateFlow()

    fun addRecentSearch(keyword: String) {
        val trimmedKeyword = keyword.trim()

        if (trimmedKeyword.isBlank()) return

        _recentSearches.value =
            (
                listOf(trimmedKeyword) +
                    _recentSearches.value.filterNot {
                        it == trimmedKeyword
                    }
                ).take(10)
    }

    fun deleteRecentSearch(keyword: String) {
        _recentSearches.value =
            _recentSearches.value.filterNot {
                it == keyword
            }
    }
}
