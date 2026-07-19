package com.umc.homefit.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.homefit.ui.component.AppScaffold

private val CardBorderColor = Color(0xFFD2D9E2)
private val EditLinkColor = Color(0xFF3C45F3)

@Composable
fun MyFinanceScreenRoute(
    viewModel: MyFinanceScreenViewModel,
    onBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
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
    onNavigateToEdit: () -> Unit = {},
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
                    MyFinanceContent(
                        sections = uiState.sections,
                        onNavigateToEdit = onNavigateToEdit,
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

@Composable
private fun MyFinanceContent(
    sections: List<FinanceInfoSection>,
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 42.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(sections, key = { it.title }) { section ->
            FinanceInfoCard(
                section = section,
                onEditClick = onNavigateToEdit
            )
        }
    }
}

@Composable
private fun FinanceInfoCard(
    section: FinanceInfoSection,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = section.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "수정",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = EditLinkColor,
                modifier = Modifier.clickable(onClick = onEditClick)
            )
        }

        Spacer(modifier = Modifier.height(17.5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CardBorderColor)
        )

        Spacer(modifier = Modifier.height(18.dp))

        section.rows.forEachIndexed { index, row ->
            if (row.value != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = row.label,
                        fontSize = 12.sp,
                        color = Color(0xFF4A4F55)
                    )
                    Text(
                        text = row.value,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF919AA4)
                    )
                }
            } else {
                Text(
                    text = row.label,
                    fontSize = 12.sp,
                    color = Color(0xFF4A4F55)
                )
            }

            if (index != section.rows.lastIndex) {
                Spacer(modifier = Modifier.height(18.dp))
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
                    rows = listOf(
                        FinanceInfoRow("연간 총소득", "4,800만 원"),
                        FinanceInfoRow("소득 유형", "근로소득")
                    )
                ),
                FinanceInfoSection(
                    title = "주택 보유 여부",
                    rows = listOf(
                        FinanceInfoRow("본인 무주택")
                    )
                )
            )
        ),
        onBack = {},
        onNavigateToEdit = {}
    )
}
