package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.mock.RecruitmentSearchMockData
import com.umc.homefit.domain.repository.recruitment.RecruitmentSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RecruitmentSearchScreenViewModel @Inject constructor(
    private val repository: RecruitmentSearchRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val uiState: StateFlow<RecruitmentSearchScreenUiState> =
        combine(
            searchQuery,
            repository.recentSearches
        ) { query, recentSearches ->
            RecruitmentSearchScreenUiState.Success(
                searchQuery = query,
                recentSearches = recentSearches,
                popularSearches = RecruitmentSearchMockData.popularSearches
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue =
                RecruitmentSearchScreenUiState.Loading
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
