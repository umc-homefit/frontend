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
fun RecruitmentDetailScreenRoute(
    viewModel: RecruitmentDetailScreenViewModel,
    onBack: () -> Unit, onNavigateToCompetition: (String) -> Unit, onNavigateToAnalysis: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    RecruitmentDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToCompetition = onNavigateToCompetition,
        onNavigateToAnalysis = onNavigateToAnalysis,

        modifier = modifier
    )
}

@Composable
fun RecruitmentDetailScreen(
    uiState: RecruitmentDetailScreenUiState,
    onBack: () -> Unit,
    onNavigateToCompetition: (String) -> Unit,
    onNavigateToAnalysis: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is RecruitmentDetailScreenUiState.Loading -> CircularProgressIndicator()
            is RecruitmentDetailScreenUiState.Success -> {
                Text(text = "RecruitmentDetailScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            androidx.compose.material3.Button(onClick = { onNavigateToCompetition("competition_123") }) { Text("View Competition (ID: competition_123)") }
            androidx.compose.material3.Button(onClick = onNavigateToAnalysis) { Text("Request Occupancy Analysis") }
            }
            is RecruitmentDetailScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentDetailScreenPreview() {
    RecruitmentDetailScreen(
        uiState = RecruitmentDetailScreenUiState.Success("Preview of RecruitmentDetailScreen"), onBack = {}, onNavigateToCompetition = {}, onNavigateToAnalysis = {}
    )
}
