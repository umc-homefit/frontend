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
fun FinanceScreenRoute(
    viewModel: FinanceScreenViewModel,
    onNavigateToRecommendedProducts: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    FinanceScreen(
        uiState = uiState,
        onNavigateToRecommendedProducts = onNavigateToRecommendedProducts,

        modifier = modifier
    )
}

@Composable
fun FinanceScreen(
    uiState: FinanceScreenUiState,
    onNavigateToRecommendedProducts: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is FinanceScreenUiState.Loading -> CircularProgressIndicator()
            is FinanceScreenUiState.Success -> {
                Text(text = "FinanceScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onNavigateToRecommendedProducts) { Text("View Recommended Financial Products") }
            }
            is FinanceScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinanceScreenPreview() {
    FinanceScreen(
        uiState = FinanceScreenUiState.Success("Preview of FinanceScreen"), onNavigateToRecommendedProducts = {}
    )
}
