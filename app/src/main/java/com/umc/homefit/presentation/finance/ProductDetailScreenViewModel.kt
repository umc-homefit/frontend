package com.umc.homefit.presentation.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.mock.ProductDetailMockData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductDetailScreenUiState>(
            ProductDetailScreenUiState.Loading
        )

    val uiState: StateFlow<ProductDetailScreenUiState> =
        _uiState.asStateFlow()

    private var loadedProductId: Long? = null

    fun loadProductDetail(
        productId: Long,
        forceRefresh: Boolean = false
    ) {
        if (
            !forceRefresh &&
            loadedProductId == productId &&
            _uiState.value is ProductDetailScreenUiState.Success
        ) {
            return
        }

        loadedProductId = productId

        viewModelScope.launch {
            _uiState.value = ProductDetailScreenUiState.Loading

            // API 응답을 기다리는 상황을 재현하기 위한 임시 지연
            delay(500)

            val product =
                ProductDetailMockData.getProductDetail(productId)

            _uiState.value = if (product != null) {
                ProductDetailScreenUiState.Success(
                    product = product
                )
            } else {
                ProductDetailScreenUiState.Error(
                    message = "해당 금융상품을 찾을 수 없습니다."
                )
            }
        }
    }

    fun retry() {
        val productId = loadedProductId ?: return

        loadProductDetail(
            productId = productId,
            forceRefresh = true
        )
    }
}
