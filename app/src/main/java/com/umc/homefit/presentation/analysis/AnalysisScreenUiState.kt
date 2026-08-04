package com.umc.homefit.presentation.analysis

sealed interface AnalysisScreenUiState {
    data object Loading : AnalysisScreenUiState
    data class Success(
        val records: List<RecordItem> = emptyList(),
        val sections: List<FinanceInfoSection> = emptyList()
    ) : AnalysisScreenUiState
    data class Error(val message: String) : AnalysisScreenUiState
}

enum class AnalysisTab(val title: String) {
    FINANCIAL_INFO("금융 정보 관리"),
    RECORD("기록")
}

data class RecordItem(
    val noticeId: String,
    val analysisId: String,
    val date: String,
    val title: String,
    val complexInfo: String,
    val areaInfo: String,
    val applyPeriod: String,
    val statusLabel: String,
    val competitionRate: String?
)
