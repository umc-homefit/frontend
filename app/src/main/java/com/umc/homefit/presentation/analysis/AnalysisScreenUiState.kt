package com.umc.homefit.presentation.analysis

sealed interface AnalysisScreenUiState {
    data object Loading : AnalysisScreenUiState
    data class Success(
        val hasFinancialInfo: Boolean = false,
        val records: List<RecordItem> = emptyList()
    ) : AnalysisScreenUiState
    data class Error(val message: String) : AnalysisScreenUiState
}

enum class AnalysisTab(val title: String) {
    FINANCIAL_INFO("금융 정보 관리"),
    RECORD("기록")
}

data class RecordItem(
    val date: String,
    val title: String,
    val complexInfo: String,
    val areaInfo: String,
    val applyPeriod: String,
    val statusLabel: String, // "모집중" | "예정" | "예정" etc.
    val competitionRate: String
)
