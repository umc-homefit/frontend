package com.umc.homefit.presentation.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    AppScaffold( // ★ 뒤로가기 아이콘 + 구분선 테스트용으로 추가
        title = "금융정보 입력",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
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
                    androidx.compose.material3.Button(onClick = onBack) { Text("Go Back") }
                    androidx.compose.material3.Button(onClick = { onNavigateToResult("result_789") }) { Text("Get Occupancy Analysis Result (ID: result_789)") }
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
        uiState = FinancialInfoScreenUiState.Success("Preview of FinancialInfoScreen"), onBack = {}, onNavigateToResult = {}
    )
}
