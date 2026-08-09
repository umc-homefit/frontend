package com.umc.homefit.presentation.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisResultDto
import com.umc.homefit.data.dto.recruitment.NoticeUnitSummary
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.domain.repository.analysis.ConditionProfileRepository
import com.umc.homefit.domain.repository.recruitment.NoticeDetailRepository
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
    private val conditionProfileRepository: ConditionProfileRepository,
    private val noticeDetailRepository: NoticeDetailRepository
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
            coroutineScope {
                // 분석 결과와 조건 프로필은 서로 의존하지 않으니 동시에 요청
                val analysisDeferred = async { analysisRepository.getEligibilityAnalysis(analysisId) }
                val profileDeferred = async { conditionProfileRepository.getConditionProfile() }

                when (val result = analysisDeferred.await()) {
                    is NetworkResult.Success -> {
                        // noticeId는 분석 응답에서만 나오는 값이라 분석 결과를 받은 뒤에 요청 시작
                        val noticeDetailDeferred = async {
                            noticeDetailRepository.getNoticeDetail(result.data.noticeId)
                        }

                        // "입력 정보"/"산정 기준" 아코디언 조회 실패는 화면 전체를 에러로 내리지 않고 빈 리스트로 대체
                        val inputInfoRows = when (val profileResult = profileDeferred.await()) {
                            is NetworkResult.Success -> profileResult.data.toInputInfoRows()
                            is NetworkResult.Error -> emptyList()
                        }
                        // 전용 면적은 Notice 상세 조회 성공 시에만 채워짐(실패해도 나머지 두 줄은 항상 표시)
                        val noticeUnits = when (val noticeResult = noticeDetailDeferred.await()) {
                            is NetworkResult.Success -> noticeResult.data.units
                            is NetworkResult.Error -> null
                        }
                        val criteriaInfoRows = result.data.toCriteriaInfoRows(noticeUnits)
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
        // 백분위 필드가 API에 없어 eligibilityScore 기반으로 클라이언트에서 10점 단위 구간 산출
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
        criteriaInfoRows = criteriaInfoRows
    )
}

/**
 * "산정 기준" — 신청 유형/신청 순위/비교 공고/전환 이율은 API에 대응 필드가 없어 제외.
 * 적용 기준일은 analyzedAt, 공급 유형은 supplyType(분석 응답 필드, MVP는 "청년안심주택" 고정)을 그대로 사용.
 * 전용 면적은 별도 Notice 상세 조회 결과(noticeUnits)에서 unitId로 찾아 붙임 — 조회 실패 시 이 줄만 빠짐.
 */
private fun EligibilityAnalysisResultDto.toCriteriaInfoRows(
    noticeUnits: List<NoticeUnitSummary>?
): List<InfoRowItem> {
    val rows = mutableListOf(
        InfoRowItem("적용 기준일", analyzedAt.toDateText()),
        InfoRowItem("공급 유형", supplyType)
    )
    noticeUnits?.find { it.unitId == unitId }?.exclusiveAreaM2?.let { area ->
        rows.add(InfoRowItem("전용 면적", area.toAreaText()))
    }
    return rows
}

/** ISO 8601("2026-08-05T15:14:46.000Z") -> "2026.08.05" */
private fun String.toDateText(): String = substringBefore("T").replace("-", ".")

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
        mapToHouseOption(housingOwnershipStatus.name) ?: if (isHomeless) "무주택" else "유주택"
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
