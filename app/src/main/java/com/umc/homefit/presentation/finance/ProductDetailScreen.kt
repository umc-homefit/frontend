package com.umc.homefit.presentation.finance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProductDetailScreenRoute(
    viewModel: ProductDetailScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    ProductDetailScreen(
        uiState = uiState,
        onBack = onBack,

        modifier = modifier
    )
}

@Composable
fun ProductDetailScreen(
    uiState: ProductDetailScreenUiState,
    onBack: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is ProductDetailScreenUiState.Loading -> CircularProgressIndicator()
            is ProductDetailScreenUiState.Success -> {
                Text(text = "ProductDetailScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            }
            is ProductDetailScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        uiState = ProductDetailScreenUiState.Success("Preview of ProductDetailScreen"), onBack = {}
    )
}
