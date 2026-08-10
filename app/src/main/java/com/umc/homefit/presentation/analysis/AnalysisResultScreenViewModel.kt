package com.umc.homefit.presentation.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisResultDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.domain.repository.analysis.ConditionProfileRepository
import com.umc.homefit.util.mapToHouseOption
import com.umc.homefit.util.toPercentileText
import com.umc.homefit.util.toWonText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisResultScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analysisRepository: AnalysisRepository,
    private val conditionProfileRepository: ConditionProfileRepository
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
            coroutineScope {
                // 분석 결과와 조건 프로필은 서로 의존하지 않으니 동시에 요청
                val analysisDeferred = async { analysisRepository.getEligibilityAnalysis(id) }
                val profileDeferred = async { conditionProfileRepository.getConditionProfile() }

                when (val result = analysisDeferred.await()) {
                    is NetworkResult.Success -> {
                        // "입력 정보" 아코디언 조회 실패는 화면 전체를 에러로 내리지 않고 빈 리스트로 대체
                        val inputInfoRows = when (val profileResult = profileDeferred.await()) {
                            is NetworkResult.Success -> profileResult.data.toInputInfoRows()
                            is NetworkResult.Error -> emptyList()
                        }
                        val criteriaInfoRows = result.data.toCriteriaInfoRows()
                        _uiState.value = AnalysisResultScreenUiState.Success(
                            data = result.data.toUiModel(
                                inputInfoRows = inputInfoRows,
                                criteriaInfoRows = criteriaInfoRows
                            )
                        )
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = AnalysisResultScreenUiState.Error(result.message)
                    }
                }
            }
        }
    }
}

private fun EligibilityAnalysisResultDto.toUiModel(
    inputInfoRows: List<InfoRowItem>,
    criteriaInfoRows: List<InfoRowItem>
): AnalysisResultData {
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
        inputInfoRows = inputInfoRows,
        criteriaInfoRows = criteriaInfoRows,
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
        rows.add(InfoRowItem("전용 면적", area.toAreaText()))
    }
    return rows
}

private fun Double.toAreaText(): String =
    if (this % 1.0 == 0.0) "${toInt()}㎡" else "${this}㎡"

private fun ConditionProfileResponse.toInputInfoRows(): List<InfoRowItem> = listOf(
    InfoRowItem("연간 총소득", (monthlyIncomeAmount * 12).toWonText()),
    InfoRowItem("총 보유 자산", totalAssetAmount.toWonText()),
    InfoRowItem("금융 자산", cashSavings.toWonText()),
    InfoRowItem("총 부채", totalDebtAmount.toWonText()),
    InfoRowItem("월 상환액", monthlyDebtPaymentAmount.toWonText()),
    InfoRowItem(
        "주택 보유 여부",
        mapToHouseOption(housingOwnershipStatus.name) ?: "정보 없음"
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
