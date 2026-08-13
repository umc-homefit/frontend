package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.mock.ProductSearchMockData
import com.umc.homefit.domain.repository.finance.ProductSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
                popularSearches = ProductSearchMockData.popularSearches
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

        searchQuery.value = trimmedKeyword
        viewModelScope.launch {
            repository.addRecentSearch(trimmedKeyword)
        }
    }

    fun deleteRecentSearch(keyword: String) {
        viewModelScope.launch {
            repository.deleteRecentSearch(keyword)
        }
    }
}
