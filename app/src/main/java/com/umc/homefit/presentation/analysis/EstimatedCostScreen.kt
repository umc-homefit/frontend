package com.umc.homefit.presentation.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EstimatedCostScreenRoute(
    viewModel: EstimatedCostScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    EstimatedCostScreen(
        uiState = uiState,
        onBack = onBack,
        
        modifier = modifier
    )
}

@Composable
fun EstimatedCostScreen(
    uiState: EstimatedCostScreenUiState,
    onBack: () -> Unit,
    
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is EstimatedCostScreenUiState.Loading -> CircularProgressIndicator()
            is EstimatedCostScreenUiState.Success -> {
                Text(text = "EstimatedCostScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            }
            is EstimatedCostScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EstimatedCostScreenPreview() {
    EstimatedCostScreen(
        uiState = EstimatedCostScreenUiState.Success("Preview of EstimatedCostScreen"), onBack = {}
    )
}
