package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisHistoryItemDto
import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import com.umc.homefit.domain.repository.analysis.ConditionProfileRepository
import com.umc.homefit.presentation.component.NoticeCardUiModel
import com.umc.homefit.util.logError
import com.umc.homefit.util.toWonText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

    fun loadConditionProfileSections() {
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
                    updateSuccessState { it.copy(records = result.data.analyses.map { item -> item.toRecordListItem() }) }
                }
                is NetworkResult.Error -> {
                    logError("기록 목록 조회 실패: ${result.message}")
                    updateSuccessState { it.copy(records = emptyList()) }
                }
            }
        }
    }

    /** records/sections가 서로 독립된 코루틴에서 도착하므로 서로를 덮어쓰지 않도록 병합해서 갱신 */
    private fun updateSuccessState(
        transform: (AnalysisScreenUiState.Success) -> AnalysisScreenUiState.Success
    ) {
        val current = _uiState.value as? AnalysisScreenUiState.Success ?: AnalysisScreenUiState.Success()
        _uiState.value = transform(current)
    }
}

private fun EligibilityAnalysisHistoryItemDto.toRecordListItem(): RecordListItem = RecordListItem(
    date = analyzedAt.toDisplayDate(),
    noticeId = noticeId.toString(),
    analysisId = analysisId.toString(),
    card = NoticeCardUiModel(
        id = analysisId.toString(),
        title = noticeTitle,
        infoLine1 = announcementNo?.let { "공고번호 | $it" } ?: unitName?.takeIf { it.isNotBlank() },
        infoLine2 = buildAreaDepositLine(exclusiveAreaM2, expectedDepositAmount),
        infoLine3 = "청약접수 | ${applicationStartAt?.toDisplayDate() ?: "공고문 참고"} ~ ${applicationEndAt?.toDisplayDate() ?: "공고문 참고"}",
        status = NoticeStatus.fromApiValue(noticeStatus),
        statusLabel = noticeStatusDisplayText,
        isSaved = false,
        // 백엔드가 아직 경쟁률 데이터를 안 주고 있어서 지금은 항상 null/빈 문자열로 옴 — 실제 값이 내려오기 시작하면 자동으로 뱃지가 뜬다.
        competitionRate = competitionRate?.takeIf { it.isNotBlank() }
    )
)

private fun buildAreaDepositLine(exclusiveAreaM2: Double?, expectedDepositAmount: Long): String {
    val area = exclusiveAreaM2?.let { "${it.toInt()}㎡" } ?: "공고문 참고"
    return "전용 $area  보증금 ${expectedDepositAmount.toWonText()}"
}

internal fun String.toDisplayDate(): String =
    Instant.parse(this).atZone(DISPLAY_ZONE).format(DISPLAY_DATE_FORMATTER)
