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
fun NotificationSettingScreenRoute(
    viewModel: NotificationSettingScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    NotificationSettingScreen(
        uiState = uiState,
        onBack = onBack,

        modifier = modifier
    )
}

@Composable
fun NotificationSettingScreen(
    uiState: NotificationSettingScreenUiState,
    onBack: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is NotificationSettingScreenUiState.Loading -> CircularProgressIndicator()
            is NotificationSettingScreenUiState.Success -> {
                Text(text = "NotificationSettingScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            }
            is NotificationSettingScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSettingScreenPreview() {
    NotificationSettingScreen(
        uiState = NotificationSettingScreenUiState.Success("Preview of NotificationSettingScreen"), onBack = {}
    )
}
