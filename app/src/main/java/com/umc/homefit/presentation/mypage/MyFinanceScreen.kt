package com.umc.homefit.presentation.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.umc.homefit.presentation.analysis.FinanceInfoRow
import com.umc.homefit.presentation.analysis.FinanceInfoSection
import com.umc.homefit.presentation.analysis.FinancialInfoContent
import com.umc.homefit.presentation.analysis.FinancialInfoStep
import com.umc.homefit.presentation.component.AppScaffold

@Composable
fun MyFinanceScreenRoute(
    viewModel: MyFinanceScreenViewModel,
    onBack: () -> Unit,
    onNavigateToEdit: (FinancialInfoStep) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // 수정 화면(FinancialInfoEdit)에서 저장하고 돌아올 때마다 이 화면이 다시 컴포지션에
    // 들어오므로, 그때마다 최신 값을 다시 조회한다. (ViewModel은 뒤로가기로 재사용되기 때문에
    // init{}의 최초 1회 호출만으로는 수정 후 값이 갱신되지 않는다)
    LaunchedEffect(Unit) {
        viewModel.loadConditionProfile()
    }

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
                        FinanceInfoRow("연간 총소득", "4,800만 원")
                    )
                )
            )
        ),
        onBack = {},
        onNavigateToEdit = {}
    )
}
