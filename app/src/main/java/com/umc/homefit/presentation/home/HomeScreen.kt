package com.umc.homefit.presentation.home

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
fun HomeScreenRoute(
    viewModel: HomeScreenViewModel,
    onNavigateToDetail: (String) -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(
        uiState = uiState,
        onNavigateToDetail = onNavigateToDetail,

        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onNavigateToDetail: (String) -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is HomeScreenUiState.Loading -> CircularProgressIndicator()
            is HomeScreenUiState.Success -> {
                Text(text = "HomeScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = { onNavigateToDetail("123") }) { Text("Go to Recruitment Detail (ID: 123)") }
            }
            is HomeScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeScreenUiState.Success("Preview of HomeScreen"), onNavigateToDetail = {}
    )
}
