package com.umc.homefit.presentation.home

sealed interface RecruitmentSearchScreenUiState {

    data object Loading : RecruitmentSearchScreenUiState

    data class Success(
        val searchQuery: String,
        val recentSearches: List<String>,
        val popularSearches: List<String>
    ) : RecruitmentSearchScreenUiState

    data class Error(
        val message: String
    ) : RecruitmentSearchScreenUiState
}
