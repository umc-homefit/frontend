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
fun AnalysisResultScreenRoute(
    viewModel: AnalysisResultScreenViewModel,
    onBack: () -> Unit, onNavigateToEstimatedCost: (String) -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalysisResultScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToEstimatedCost = onNavigateToEstimatedCost,
        
        modifier = modifier
    )
}

@Composable
fun AnalysisResultScreen(
    uiState: AnalysisResultScreenUiState,
    onBack: () -> Unit,
    onNavigateToEstimatedCost: (String) -> Unit,
    
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is AnalysisResultScreenUiState.Loading -> CircularProgressIndicator()
            is AnalysisResultScreenUiState.Success -> {
                Text(text = "AnalysisResultScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            androidx.compose.material3.Button(onClick = { onNavigateToEstimatedCost("cost_abc") }) { Text("View Estimated Deposit/Rent (ID: cost_abc)") }
            }
            is AnalysisResultScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalysisResultScreenPreview() {
    AnalysisResultScreen(
        uiState = AnalysisResultScreenUiState.Success("Preview of AnalysisResultScreen"), onBack = {}, onNavigateToEstimatedCost = {}
    )
}
