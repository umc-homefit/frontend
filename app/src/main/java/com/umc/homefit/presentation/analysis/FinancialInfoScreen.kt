package com.umc.homefit.presentation.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.umc.homefit.ui.component.AppScaffold

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
    AppScaffold(
        title = null,
        showBackButton = true,
        onBackClick = onBack,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is FinancialInfoScreenUiState.Loading -> CircularProgressIndicator()

                is FinancialInfoScreenUiState.Success -> {
                    Text(text = "FinancialInfoScreen: ${uiState.data}")
                }

                is FinancialInfoScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinancialInfoScreenPreview() {
    FinancialInfoScreen(
        uiState = FinancialInfoScreenUiState.Success("Preview of FinancialInfoScreen"),
        onBack = {},
        onNavigateToResult = {}
    )
}
