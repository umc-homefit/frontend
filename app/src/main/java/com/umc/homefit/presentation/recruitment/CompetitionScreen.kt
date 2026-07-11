package com.umc.homefit.presentation.recruitment

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
fun CompetitionScreenRoute(
    viewModel: CompetitionScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    CompetitionScreen(
        uiState = uiState,
        onBack = onBack,

        modifier = modifier
    )
}

@Composable
fun CompetitionScreen(
    uiState: CompetitionScreenUiState,
    onBack: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is CompetitionScreenUiState.Loading -> CircularProgressIndicator()
            is CompetitionScreenUiState.Success -> {
                Text(text = "CompetitionScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            }
            is CompetitionScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompetitionScreenPreview() {
    CompetitionScreen(
        uiState = CompetitionScreenUiState.Success("Preview of CompetitionScreen"), onBack = {}
    )
}
