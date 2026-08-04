package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisHistoryItemDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.util.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private const val RECORD_PAGE_SIZE = 50
private val DISPLAY_ZONE = ZoneId.of("Asia/Seoul")
private val DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd")

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository
) : ViewModel() {
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

        _uiState.value = AnalysisScreenUiState.Success(sections = sampleSections)
        loadRecords(sampleSections)
    }

    private fun loadRecords(sections: List<FinanceInfoSection>) {
        viewModelScope.launch {
            when (val result = analysisRepository.getMyEligibilityAnalyses(page = 0, size = RECORD_PAGE_SIZE)) {
                is NetworkResult.Success -> {
                    _uiState.value = AnalysisScreenUiState.Success(
                        sections = sections,
                        records = result.data.analyses.map { it.toRecordItem() }
                    )
                }
                is NetworkResult.Error -> {
                    logError("기록 목록 조회 실패: ${result.message}")
                    _uiState.value = AnalysisScreenUiState.Success(sections = sections, records = emptyList())
                }
            }
        }
    }
}

private fun EligibilityAnalysisHistoryItemDto.toRecordItem(): RecordItem = RecordItem(
    noticeId = noticeId.toString(),
    date = analyzedAt.toDisplayDate(),
    title = noticeTitle,
    complexInfo = announcementNo?.let { "공고번호 | $it" } ?: unitName.orEmpty(),
    areaInfo = buildAreaInfo(exclusiveAreaM2, expectedDepositAmount),
    applyPeriod = buildApplyPeriod(applicationStartAt, applicationEndAt),
    statusLabel = noticeStatusDisplayText,
    competitionRate = competitionRate
)

private fun buildAreaInfo(exclusiveAreaM2: Double?, expectedDepositAmount: Long): String {
    val depositInManwon = NumberFormat.getNumberInstance(Locale.KOREA).format(expectedDepositAmount / 10_000)
    return if (exclusiveAreaM2 != null) {
        "전용 ${exclusiveAreaM2.toInt()}㎡ · 보증금 ${depositInManwon}만원"
    } else {
        "보증금 ${depositInManwon}만원"
    }
}

private fun buildApplyPeriod(startAt: String?, endAt: String?): String {
    if (startAt == null || endAt == null) return "청약접수 정보 없음"
    return "청약접수 | ${startAt.toDisplayDate()} ~ ${endAt.toDisplayDate()}"
}

private fun String.toDisplayDate(): String =
    Instant.parse(this).atZone(DISPLAY_ZONE).format(DISPLAY_DATE_FORMATTER)
