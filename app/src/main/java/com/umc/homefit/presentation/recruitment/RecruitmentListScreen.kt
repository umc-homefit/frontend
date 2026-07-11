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
fun RecruitmentListScreenRoute(
    viewModel: RecruitmentListScreenViewModel,
    onNavigateToFilter: () -> Unit, onNavigateToDetail: (String) -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    RecruitmentListScreen(
        uiState = uiState,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToFilter = onNavigateToFilter,

        modifier = modifier
    )
}

@Composable
fun RecruitmentListScreen(
    uiState: RecruitmentListScreenUiState,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFilter: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is RecruitmentListScreenUiState.Loading -> CircularProgressIndicator()
            is RecruitmentListScreenUiState.Success -> {
                Text(text = "RecruitmentListScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onNavigateToFilter) { Text("Go to Filter") }
            androidx.compose.material3.Button(onClick = { onNavigateToDetail("456") }) { Text("Go to Detail (ID: 456)") }
            }
            is RecruitmentListScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentListScreenPreview() {
    RecruitmentListScreen(
        uiState = RecruitmentListScreenUiState.Success("Preview of RecruitmentListScreen"), onNavigateToDetail = {}, onNavigateToFilter = {}
    )
}
