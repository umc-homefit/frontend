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
fun MyFinanceScreenRoute(
    viewModel: MyFinanceScreenViewModel,
    onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    MyFinanceScreen(
        uiState = uiState,
        onBack = onBack,

        modifier = modifier
    )
}

@Composable
fun MyFinanceScreen(
    uiState: MyFinanceScreenUiState,
    onBack: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is MyFinanceScreenUiState.Loading -> CircularProgressIndicator()
            is MyFinanceScreenUiState.Success -> {
                Text(text = "MyFinanceScreen: ${uiState.data}")
            androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
            }
            is MyFinanceScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyFinanceScreenPreview() {
    MyFinanceScreen(
        uiState = MyFinanceScreenUiState.Success("Preview of MyFinanceScreen"), onBack = {}
    )
}
