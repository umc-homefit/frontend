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
fun FinancialInfoScreenRoute(
    viewModel: FinancialInfoScreenViewModel,
    onBack: () -> Unit, onNavigateToResult: (String) -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    FinancialInfoScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToResult = onNavigateToResult,

        modifier = modifier
    )
}

@Composable
fun FinancialInfoScreen(
    uiState: FinancialInfoScreenUiState,
    onBack: () -> Unit,
    onNavigateToResult: (String) -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is FinancialInfoScreenUiState.Loading -> CircularProgressIndicator()
            is FinancialInfoScreenUiState.Success -> {
                Text(text = "FinancialInfoScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            androidx.compose.material3.Button(onClick = { onNavigateToResult("result_789") }) { Text("Get Occupancy Analysis Result (ID: result_789)") }
            }
            is FinancialInfoScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinancialInfoScreenPreview() {
    FinancialInfoScreen(
        uiState = FinancialInfoScreenUiState.Success("Preview of FinancialInfoScreen"), onBack = {}, onNavigateToResult = {}
    )
}
