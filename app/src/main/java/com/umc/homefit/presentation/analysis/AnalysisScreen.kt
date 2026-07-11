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
fun AnalysisScreenRoute(
    viewModel: AnalysisScreenViewModel,
    onNavigateToFinancialInfo: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalysisScreen(
        uiState = uiState,
        onNavigateToFinancialInfo = onNavigateToFinancialInfo,
        
        modifier = modifier
    )
}

@Composable
fun AnalysisScreen(
    uiState: AnalysisScreenUiState,
    onNavigateToFinancialInfo: () -> Unit,
    
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is AnalysisScreenUiState.Loading -> CircularProgressIndicator()
            is AnalysisScreenUiState.Success -> {
                Text(text = "AnalysisScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onNavigateToFinancialInfo) { Text("Enter Financial Info") }
            }
            is AnalysisScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen(
        uiState = AnalysisScreenUiState.Success("Preview of AnalysisScreen"), onNavigateToFinancialInfo = {}
    )
}
