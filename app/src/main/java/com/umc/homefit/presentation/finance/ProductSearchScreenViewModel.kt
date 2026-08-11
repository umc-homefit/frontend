package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.domain.repository.finance.ProductSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ProductSearchScreenViewModel @Inject constructor(
    private val repository: ProductSearchRepository
) : ViewModel() {

    private val searchQuery =
        MutableStateFlow("")

    val uiState: StateFlow<ProductSearchScreenUiState> =
        combine(
            searchQuery,
            repository.recentSearches
        ) { query, recentSearches ->
            ProductSearchScreenUiState.Success(
                searchQuery = query,
                recentSearches = recentSearches,
                popularSearches = listOf(
                    "디딤돌 대출",
                    "버팀목 전세대출",
                    "청년 주택드림 청약통장",
                    "주택청약종합저축",
                    "국민은행"
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductSearchScreenUiState.Loading
        )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun addRecentSearch(keyword: String) {
        val trimmedKeyword = keyword.trim()

        if (trimmedKeyword.isBlank()) return

        repository.addRecentSearch(trimmedKeyword)
        searchQuery.value = trimmedKeyword
    }

    fun deleteRecentSearch(keyword: String) {
        repository.deleteRecentSearch(keyword)
    }
}
