package com.umc.homefit.presentation.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.ConditionProfileSnapshotDto
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisResultDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.util.mapToHouseOption
import com.umc.homefit.util.toAreaText
import com.umc.homefit.util.toPercentileText
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

    private val analysisId: Long? = (savedStateHandle["analysisId"] as? String)?.toLongOrNull()

    private val _uiState = MutableStateFlow<AnalysisResultScreenUiState>(AnalysisResultScreenUiState.Loading)
    val uiState: StateFlow<AnalysisResultScreenUiState> = _uiState.asStateFlow()

    init {
        loadAnalysisResult()
    }

    private fun loadAnalysisResult() {
        val id = analysisId
        if (id == null) {
            _uiState.value = AnalysisResultScreenUiState.Error("잘못된 분석 결과 접근입니다")
            return
        }
        viewModelScope.launch {
            _uiState.value = AnalysisResultScreenUiState.Loading
            when (val result = analysisRepository.getEligibilityAnalysis(id)) {
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
    val snapshot = conditionProfileSnapshot
    return AnalysisResultData(
        probabilityGrade = resultLevel.toGradeText(),
        percentileText = eligibilityScore.toPercentileText(),
        score = eligibilityScore,
        expectedDeposit = expectedDepositAmount.toWonText(),
        expectedMonthlyRent = expectedMonthlyRentAmount.toWonText(),
        criteriaStatus = conditionResults.map { condition ->
            CriteriaItem(
                title = condition.conditionName,
                statusText = condition.resultStatus.toStatusText(),
                resultStatus = condition.resultStatus
            )
        },
        inputInfoRows = snapshot?.toInputInfoRows() ?: emptyList(),
        inputInfoUnavailableMessage = if (snapshot == null) "분석 당시 입력 정보를 사용할 수 없습니다" else null,
        criteriaInfoRows = toCriteriaInfoRows(),
        shareText = buildString {
            append("[HomeFit] 입주 분석 결과\n")
            append("입주 가능성: ${resultLevel.toGradeText()} (${eligibilityScore}점)\n")
            append("예상 보증금 ${expectedDepositAmount.toWonText()} / 월세 ${expectedMonthlyRentAmount.toWonText()}")
        }
    )
}

private fun EligibilityAnalysisResultDto.toCriteriaInfoRows(): List<InfoRowItem> {
    val rows = mutableListOf(
        InfoRowItem("적용 기준일", analyzedAt.toDisplayDate()),
        InfoRowItem("공급 유형", supplyType)
    )
    exclusiveAreaM2?.let { area ->
        rows.add(InfoRowItem("전용 면적", "${area.toAreaText()}㎡"))
    }
    return rows
}

private fun ConditionProfileSnapshotDto.toInputInfoRows(): List<InfoRowItem> = listOf(
    InfoRowItem("연간 총소득", (monthlyIncomeAmount * 12).toWonText()),
    InfoRowItem("총 보유 자산", totalAssetAmount.toWonText()),
    InfoRowItem("금융 자산", cashSavings.toWonText()),
    InfoRowItem("총 부채", totalDebtAmount.toWonText()),
    InfoRowItem("월 상환액", monthlyDebtPaymentAmount.toWonText()),
    InfoRowItem(
        "주택 보유 여부",
        housingOwnershipStatus?.let { mapToHouseOption(it.name) } ?: "정보 없음"
    )
)

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
    "FAIL" -> "미적합"
    "NEED_CHECK" -> "확인 필요"
    else -> this
}
