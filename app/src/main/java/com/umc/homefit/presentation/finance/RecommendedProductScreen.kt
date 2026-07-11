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
fun RecommendedProductScreenRoute(
    viewModel: RecommendedProductScreenViewModel,
    onBack: () -> Unit, onNavigateToDetail: (String) -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    RecommendedProductScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToDetail = onNavigateToDetail,

        modifier = modifier
    )
}

@Composable
fun RecommendedProductScreen(
    uiState: RecommendedProductScreenUiState,
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is RecommendedProductScreenUiState.Loading -> CircularProgressIndicator()
            is RecommendedProductScreenUiState.Success -> {
                Text(text = "RecommendedProductScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            androidx.compose.material3.Button(onClick = { onNavigateToDetail("product_def") }) { Text("View Product Detail (ID: product_def)") }
            }
            is RecommendedProductScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendedProductScreenPreview() {
    RecommendedProductScreen(
        uiState = RecommendedProductScreenUiState.Success("Preview of RecommendedProductScreen"), onBack = {}, onNavigateToDetail = {}
    )
}
