package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import com.umc.homefit.data.mock.FinanceMockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecommendedProductScreenViewModel @Inject constructor() :
    ViewModel() {

    private val _uiState =
        MutableStateFlow<RecommendedProductScreenUiState>(
            RecommendedProductScreenUiState.Loading
        )

    val uiState: StateFlow<RecommendedProductScreenUiState> =
        _uiState.asStateFlow()

    init {
        loadMockProducts()
    }

    private fun loadMockProducts() {
        _uiState.value =
            RecommendedProductScreenUiState.Success(
                products = FinanceMockData.recommendedProducts
                    .filter { product ->
                        product.isEligible
                    }
            )
    }
}
