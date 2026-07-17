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
fun RecruitmentFilterScreenRoute(
    viewModel: RecruitmentFilterScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    RecruitmentFilterScreen(
        uiState = uiState,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun RecruitmentFilterScreen(
    uiState: RecruitmentFilterScreenUiState,
    onBack: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is RecruitmentFilterScreenUiState.Loading -> CircularProgressIndicator()
            is RecruitmentFilterScreenUiState.Success -> {
                Text(text = "RecruitmentFilterScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            }
            is RecruitmentFilterScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentFilterScreenPreview() {
    RecruitmentFilterScreen(
        uiState = RecruitmentFilterScreenUiState.Success("Preview of RecruitmentFilterScreen"), onBack = {}
    )
}
