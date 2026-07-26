package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<AnalysisScreenUiState>(AnalysisScreenUiState.Loading)
    val uiState: StateFlow<AnalysisScreenUiState> = _uiState.asStateFlow()

    init {
        val sampleSections = listOf(
            FinanceInfoSection(
                title = "소득 정보",
                step = FinancialInfoStep.INCOME,
                rows = listOf(
                    FinanceInfoRow("연간 총소득", "4,800만 원"),
                    FinanceInfoRow("소득 유형", "근로소득")
                )
            ),
            FinanceInfoSection(
                title = "자산 정보",
                step = FinancialInfoStep.ASSET,
                rows = listOf(
                    FinanceInfoRow("총 보유 자산", "6,500만 원"),
                    FinanceInfoRow("금융 자산", "2,800만 원")
                )
            ),
            FinanceInfoSection(
                title = "부채 정보",
                step = FinancialInfoStep.DEBT,
                rows = listOf(
                    FinanceInfoRow("총 부채 금액", "1,800만 원"),
                    FinanceInfoRow("월 상환액", "35만 원")
                )
            ),
            FinanceInfoSection(
                title = "주택 보유 여부",
                step = FinancialInfoStep.HOUSE,
                rows = listOf(
                    FinanceInfoRow("본인 무주택")
                )
            )
        )

        val sampleRecords = listOf(
            RecordItem(
                recruitmentId = "1",
                date = "2026.07.07",
                title = "강동구 청년안심주택 2025-03호",
                complexInfo = "공고번호 : 2024-강동-031",
                areaInfo = "전용 59㎡ · 보증금 3,200만원",
                applyPeriod = "청약접수 : 2026.07.05 ~ 2026.07.08",
                statusLabel = "모집중",
                competitionRate = "12:1"
            ),
            RecordItem(
                recruitmentId = "2",
                date = "2026.07.01",
                title = "강동구 고덕강일 청년안심주택",
                complexInfo = "공고번호 : 2024-강동-031",
                areaInfo = "전용 59㎡ · 보증금 3,200만원",
                applyPeriod = "청약접수 : 2026.07.05 ~ 2026.07.08",
                statusLabel = "예정",
                competitionRate = "12:1"
            )
        )

        // Success 상태에 더미 데이터 리스트를 주입
        _uiState.value = AnalysisScreenUiState.Success(
            sections = sampleSections,
            records = sampleRecords
        )
    }
}
