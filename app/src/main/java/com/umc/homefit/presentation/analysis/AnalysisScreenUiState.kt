package com.umc.homefit.presentation.analysis

import com.umc.homefit.presentation.component.NoticeCardUiModel

sealed interface AnalysisScreenUiState {
    data object Loading : AnalysisScreenUiState
    data class Success(
        val records: List<RecordListItem> = emptyList(),
        val sections: List<FinanceInfoSection> = emptyList()
    ) : AnalysisScreenUiState
    data class Error(val message: String) : AnalysisScreenUiState
}

enum class AnalysisTab(val title: String) {
    FINANCIAL_INFO("금융 정보 관리"),
    RECORD("기록")
}

data class RecordListItem(
    val date: String,
    val noticeId: String,
    val analysisId: String,
    val card: NoticeCardUiModel
)
