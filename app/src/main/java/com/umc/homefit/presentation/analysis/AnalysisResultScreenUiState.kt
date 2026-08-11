package com.umc.homefit.presentation.analysis

sealed interface AnalysisResultScreenUiState {
    object Loading : AnalysisResultScreenUiState
    data class Error(val message: String) : AnalysisResultScreenUiState
    data class Success(val data: AnalysisResultData) : AnalysisResultScreenUiState
}

data class AnalysisResultData(
    val probabilityGrade: String,
    val percentileText: String,
    val score: Int,
    val expectedDeposit: String,
    val expectedMonthlyRent: String,
    val criteriaStatus: List<CriteriaItem>,
    val inputInfoRows: List<InfoRowItem> = emptyList(),
    val inputInfoUnavailableMessage: String? = null,
    val criteriaInfoRows: List<InfoRowItem> = emptyList(),
    val shareText: String
)

data class CriteriaItem(
    val title: String,
    val statusText: String,
    val resultStatus: String
)

data class InfoRowItem(
    val title: String,
    val value: String
)
