package com.umc.homefit.presentation.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.umc.homefit.presentation.analysis.FinanceInfoRow
import com.umc.homefit.presentation.analysis.FinanceInfoSection
import com.umc.homefit.presentation.analysis.FinancialInfoContent
import com.umc.homefit.presentation.analysis.FinancialInfoStep
import com.umc.homefit.ui.component.AppScaffold

@Composable
fun MyFinanceScreenRoute(
    viewModel: MyFinanceScreenViewModel,
    onBack: () -> Unit,
    onNavigateToEdit: (FinancialInfoStep) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    MyFinanceScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToEdit = onNavigateToEdit,
        modifier = modifier
    )
}

@Composable
fun MyFinanceScreen(
    uiState: MyFinanceScreenUiState,
    onBack: () -> Unit,
    onNavigateToEdit: (FinancialInfoStep) -> Unit = {},
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = "금융 정보 관리",
        showBackButton = true,
        onBackClick = onBack,
        showDivider = true,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is MyFinanceScreenUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MyFinanceScreenUiState.Success -> {
                    FinancialInfoContent(
                        sections = uiState.sections,
                        onEditClick = onNavigateToEdit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is MyFinanceScreenUiState.Error -> {
                    Text(text = "Error: ${uiState.message}", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyFinanceScreenPreview() {
    MyFinanceScreen(
        uiState = MyFinanceScreenUiState.Success(
            sections = listOf(
                FinanceInfoSection(
                    title = "소득 정보",
                    step = FinancialInfoStep.INCOME,
                    rows = listOf(
                        FinanceInfoRow("연간 총소득", "4,800만 원"),
                        FinanceInfoRow("소득 유형", "근로소득")
                    )
                )
            )
        ),
        onBack = {},
        onNavigateToEdit = {}
    )
}
