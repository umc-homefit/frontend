package com.umc.homefit.presentation.recruitment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.umc.homefit.data.dto.RecruitmentDto
import com.umc.homefit.data.dto.RecruitmentStatus

@Composable
fun RecruitmentDetailScreenRoute(
    viewModel: RecruitmentDetailScreenViewModel,
    onBack: () -> Unit, onNavigateToCompetition: (String) -> Unit, onNavigateToAnalysis: () -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    RecruitmentDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToCompetition = onNavigateToCompetition,
        onNavigateToAnalysis = onNavigateToAnalysis,

        modifier = modifier
    )
}

@Composable
fun RecruitmentDetailScreen(
    uiState: RecruitmentDetailScreenUiState,
    onBack: () -> Unit,
    onNavigateToCompetition: (String) -> Unit,
    onNavigateToAnalysis: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is RecruitmentDetailScreenUiState.Loading -> CircularProgressIndicator()
            is RecruitmentDetailScreenUiState.Success -> {
                val recruitment = uiState.recruitment
                Text(text = recruitment.title)
                Text(text = recruitment.company)
                Text(text = recruitment.location)
                Text(text = "${recruitment.rentType} / 보증금 ${recruitment.deposit} / 월세 ${recruitment.monthlyRent}")
                Text(text = recruitment.announcementDate)
                Button(onClick = onBack) { Text("Go Back") }
                Button(onClick = { onNavigateToCompetition(recruitment.id) }) { Text("View Competition (ID: ${recruitment.id})") }
                Button(onClick = onNavigateToAnalysis) { Text("Request Occupancy Analysis") }
            }
            is RecruitmentDetailScreenUiState.Error -> Text(text = "Error: ${uiState.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecruitmentDetailScreenPreview() {
    RecruitmentDetailScreen(
        uiState = RecruitmentDetailScreenUiState.Success(
            recruitment = RecruitmentDto(
                id = "1",
                title = "2026년 행복주택 입주자 모집공고",
                company = "한국토지주택공사",
                location = "서울특별시 강남구",
                rentType = "월세",
                deposit = 30000000,
                monthlyRent = 350000,
                announcementDate = "2026-07-13",
                announcementNumber = "2026-강남-001",
                area = 39.87,
                applicationStartDate = "2026-07-14",
                applicationEndDate = "2026-07-18",
                status = RecruitmentStatus.RECRUITING,
                competitionRate = "12.3:1"
            )
        ),
        onBack = {}, onNavigateToCompetition = {}, onNavigateToAnalysis = {}
    )
}
