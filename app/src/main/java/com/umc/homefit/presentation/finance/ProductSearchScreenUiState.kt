package com.umc.homefit.presentation.finance

sealed interface ProductSearchScreenUiState {

    object Loading : ProductSearchScreenUiState

    data class Success(
        val searchQuery: String,
        val recentSearches: List<String>,
        val popularSearches: List<String>
    ) : ProductSearchScreenUiState

    data class Error(
        val message: String
    ) : ProductSearchScreenUiState
}
