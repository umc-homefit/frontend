package com.umc.homefit.presentation.mypage

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
fun MyPageScreenRoute(
    viewModel: MyPageScreenViewModel,
    onNavigateToSaved: () -> Unit, onNavigateToNotification: () -> Unit, onNavigateToFinance: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    MyPageScreen(
        uiState = uiState,
        onNavigateToSaved = onNavigateToSaved,
        onNavigateToNotification = onNavigateToNotification,
        onNavigateToFinance = onNavigateToFinance,

        modifier = modifier
    )
}

@Composable
fun MyPageScreen(
    uiState: MyPageScreenUiState,
    onNavigateToSaved: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToFinance: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is MyPageScreenUiState.Loading -> CircularProgressIndicator()
            is MyPageScreenUiState.Success -> {
                Text(text = "MyPageScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onNavigateToSaved) { Text("Manage Saved Recruitments") }
            androidx.compose.material3.Button(onClick = onNavigateToNotification) { Text("Notification Settings") }
            androidx.compose.material3.Button(onClick = onNavigateToFinance) { Text("Manage My Finance Info") }
            }
            is MyPageScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(
        uiState = MyPageScreenUiState.Success("Preview of MyPageScreen"), onNavigateToSaved = {}, onNavigateToNotification = {}, onNavigateToFinance = {}
    )
}
