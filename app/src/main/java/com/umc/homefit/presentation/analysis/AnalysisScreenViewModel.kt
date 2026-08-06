package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisHistoryItemDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.domain.repository.finance.ConditionProfileRepository
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
    private val analysisRepository: AnalysisRepository,
    private val conditionProfileRepository: ConditionProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AnalysisScreenUiState>(AnalysisScreenUiState.Loading)
    val uiState: StateFlow<AnalysisScreenUiState> = _uiState.asStateFlow()

    init {
        loadConditionProfileSections()
        loadRecords()
    }

    /** 금융 정보 관리 탭 요약 섹션. 프로필이 없는 계정(대부분 FINANCE404)은 "없음"으로 채운다. */
    private fun loadConditionProfileSections() {
        viewModelScope.launch {
            val sections = when (val result = conditionProfileRepository.getConditionProfile()) {
                is NetworkResult.Success -> result.data.toFinanceInfoSections()
                is NetworkResult.Error -> emptyFinanceInfoSections()
            }
            updateSuccessState { it.copy(sections = sections) }
        }
    }

    private fun loadRecords() {
        viewModelScope.launch {
            when (val result = analysisRepository.getMyEligibilityAnalyses(page = 0, size = RECORD_PAGE_SIZE)) {
                is NetworkResult.Success -> {
                    updateSuccessState { it.copy(records = result.data.analyses.map { item -> item.toRecordItem() }) }
                }
                is NetworkResult.Error -> {
                    logError("기록 목록 조회 실패: ${result.message}")
                    updateSuccessState { it.copy(records = emptyList()) }
                }
            }
        }
    }

    /** records/sections가 서로 독립된 코루틴에서 도착하므로, 서로를 덮어쓰지 않도록 병합해서 갱신한다. */
    private fun updateSuccessState(
        transform: (AnalysisScreenUiState.Success) -> AnalysisScreenUiState.Success
    ) {
        val current = _uiState.value as? AnalysisScreenUiState.Success ?: AnalysisScreenUiState.Success()
        _uiState.value = transform(current)
    }
}

private fun EligibilityAnalysisHistoryItemDto.toRecordItem(): RecordItem = RecordItem(
    noticeId = noticeId.toString(),
    analysisId = analysisId.toString(),
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
