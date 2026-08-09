package com.umc.homefit.presentation.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisResultDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.util.toWonText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisResultScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    private val analysisId: Long = checkNotNull(savedStateHandle["analysisId"]).toString().toLong()

    private val _uiState = MutableStateFlow<AnalysisResultScreenUiState>(AnalysisResultScreenUiState.Loading)
    val uiState: StateFlow<AnalysisResultScreenUiState> = _uiState.asStateFlow()

    init {
        loadAnalysisResult()
    }

    private fun loadAnalysisResult() {
        viewModelScope.launch {
            _uiState.value = AnalysisResultScreenUiState.Loading
            when (val result = analysisRepository.getEligibilityAnalysis(analysisId)) {
                is NetworkResult.Success -> {
                    _uiState.value = AnalysisResultScreenUiState.Success(data = result.data.toUiModel())
                }
                is NetworkResult.Error -> {
                    _uiState.value = AnalysisResultScreenUiState.Error(result.message)
                }
            }
        }
    }
}

private fun EligibilityAnalysisResultDto.toUiModel(): AnalysisResultData {
    return AnalysisResultData(
        probabilityGrade = resultLevel.toGradeText(),
        // TODO: #72 문의 1 — 백분위 필드가 API에 없음. 답변 오면 채우기
        percentileText = "",
        score = eligibilityScore,
        expectedDeposit = expectedDepositAmount.toWonText(),
        expectedMonthlyRent = expectedMonthlyRentAmount.toWonText(),
        criteriaStatus = conditionResults.map { condition ->
            CriteriaItem(
                title = condition.conditionName,
                statusText = condition.resultStatus.toStatusText(),
                isSuitable = condition.resultStatus == "PASS"
            )
        },
        inputInfoRows = conditionResults
            .filter { !it.userValue.isNullOrBlank() }
            .map { condition -> InfoRowItem(title = condition.conditionName, value = condition.userValue!!) }
    )
}

private fun String.toGradeText(): String = when (this) {
    "HIGH" -> "높음"
    "MEDIUM" -> "보통"
    "LOW" -> "낮음"
    "NOT_ELIGIBLE" -> "해당 없음"
    "NEED_CHECK" -> "확인 필요"
    else -> this
}

private fun String.toStatusText(): String = when (this) {
    "PASS" -> "적합"
    "FAIL" -> "부적합"
    "NEED_CHECK" -> "확인 필요"
    else -> this
}
