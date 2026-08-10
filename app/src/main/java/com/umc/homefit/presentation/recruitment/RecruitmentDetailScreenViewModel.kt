package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.recruitment.NoticeConditionDto
import com.umc.homefit.data.dto.recruitment.NoticeDetailResponse
import com.umc.homefit.data.dto.recruitment.NoticeFileDto
import com.umc.homefit.data.dto.recruitment.NoticeUnitDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import com.umc.homefit.util.error.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private const val FALLBACK_TEXT = "공고문 참고"
private val DISPLAY_ZONE = ZoneId.of("Asia/Seoul")
private val DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd")

@HiltViewModel
class RecruitmentDetailScreenViewModel @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
    private val savedNoticeRepository: SavedNoticeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noticeId: Long? = savedStateHandle.get<String>("recruitmentId")?.toLongOrNull()

    private val _uiState = MutableStateFlow<RecruitmentDetailScreenUiState>(RecruitmentDetailScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentDetailScreenUiState> = _uiState.asStateFlow()

    init {
        loadRecruitmentDetail()
    }

    fun retry() = loadRecruitmentDetail()

    private fun loadRecruitmentDetail() {
        val id = noticeId
        if (id == null) {
            // 잘못된 noticeId로 진입한 경우 재시도로 해결될 문제가 아니라 "준비 중" 화면과 동일하게 처리
            _uiState.value = RecruitmentDetailScreenUiState.Error(
                message = "잘못된 공고 정보입니다.",
                errorCode = ErrorCode.COMMON404
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = RecruitmentDetailScreenUiState.Loading
            _uiState.value = when (val result = recruitmentRepository.getRecruitmentDetail(id)) {
                is NetworkResult.Success -> RecruitmentDetailScreenUiState.Success(result.data.toRecruitmentDetailUiModel())
                is NetworkResult.Error -> RecruitmentDetailScreenUiState.Error(
                    message = result.message,
                    errorCode = result.errorCode
                )
            }
        }
    }

    fun toggleBookmark() {
        val id = noticeId ?: return
        val currentState = _uiState.value
        if (currentState !is RecruitmentDetailScreenUiState.Success) return

        val nextSaved = !currentState.recruitment.isSaved
        _uiState.value = currentState.copy(recruitment = currentState.recruitment.copy(isSaved = nextSaved))

        viewModelScope.launch {
            val isSuccess = if (nextSaved) {
                savedNoticeRepository.saveNotice(id) is NetworkResult.Success
            } else {
                savedNoticeRepository.unsaveNotice(id) is NetworkResult.Success
            }
            if (!isSuccess) {
                val rollbackState = _uiState.value
                if (rollbackState is RecruitmentDetailScreenUiState.Success) {
                    _uiState.value = rollbackState.copy(
                        recruitment = rollbackState.recruitment.copy(isSaved = !nextSaved)
                    )
                }
            }
        }
    }

    private fun NoticeDetailResponse.toRecruitmentDetailUiModel(): RecruitmentDetailUiModel {
        return RecruitmentDetailUiModel(
            noticeId = noticeId,
            title = title,
            status = status,
            statusDisplayText = statusDisplayText,
            isSaved = isSaved,
            sourceUrl = sourceUrl,
            // targetType을 한글 라벨로 매핑해 뱃지에 표기, "기타"(미분류)면 뱃지 자체를 표시하지 않음
            targetTypeBadgeText = conditions.firstOrNull()?.targetType?.toTargetTypeLabel()?.takeIf { it != "기타" },
            // 공급 정보 카드: 서버가 "공급 유형"/"입주 예정일"은 아직 내려주지 않아 공고문 참고로 대체
            supplyLocation = address ?: listOfNotNull(region, district).joinToString(" ").ifBlank { FALLBACK_TEXT },
            supplyType = FALLBACK_TEXT,
            unitSummary = units.toUnitSummaryText(),
            depositRangeText = formatWonRange(units.mapNotNull { it.depositMin }.minOrNull(), units.mapNotNull { it.depositMax }.maxOrNull()),
            monthlyRentRangeText = formatWonRange(units.mapNotNull { it.monthlyRentMin }.minOrNull(), units.mapNotNull { it.monthlyRentMax }.maxOrNull()),
            moveInDate = FALLBACK_TEXT,
            // 자격 조건 카드: conditions가 여러 대상유형(청년/신혼부부 등)으로 나뉘어 올 경우 유형별로 병기
            ageRange = conditions.toConditionFieldText { formatAgeRange(it.minAge, it.maxAge) },
            incomeStandard = conditions.toConditionFieldText { it.incomeLimitText ?: it.incomeLimitAmount?.let { amt -> "${formatWon(amt)} 이하" } ?: FALLBACK_TEXT },
            assetStandard = conditions.toConditionFieldText { it.assetLimitText ?: it.assetLimitAmount?.let { amt -> "${formatWon(amt)} 이하" } ?: FALLBACK_TEXT },
            housingOwnership = conditions.toConditionFieldText { it.requiresHomeless?.let { req -> if (req) "완전 무주택" else "제한 없음" } ?: FALLBACK_TEXT },
            residencyRequirement = conditions.toConditionFieldText { it.residenceRequirement ?: FALLBACK_TEXT },
            // 신청 기간 카드: "당첨자 발표"/"계약 체결"은 서버 응답에 없어 공고문 참고로 대체
            applicationStartText = applicationStartAt.toDisplayDate(),
            applicationEndText = applicationEndAt.toDisplayDate(),
            winnerAnnouncementDate = FALLBACK_TEXT,
            contractPeriod = FALLBACK_TEXT,
            // 사진(IMAGE)도 위 사진 그리드와 별개로 첨부파일 목록에 함께 노출
            attachments = files.map { it.toAttachmentRow() },
            photoUrls = files.filter { it.fileType == "IMAGE" }.map { it.fileUrl }
        )
    }

    private fun List<NoticeUnitDto>.toUnitSummaryText(): String {
        if (isEmpty()) return FALLBACK_TEXT
        return joinToString(" / ") { unit ->
            val area = unit.exclusiveAreaM2?.let { "전용 ${it}㎡" } ?: unit.unitName
            val count = unit.supplyCount?.let { "${it}세대" }
            listOfNotNull(area, count).joinToString(" ")
        }
    }

    private fun List<NoticeConditionDto>.toConditionFieldText(selector: (NoticeConditionDto) -> String): String {
        return when (size) {
            0 -> FALLBACK_TEXT
            1 -> selector(first())
            else -> joinToString(" / ") { "${it.targetType.toTargetTypeLabel()}: ${selector(it)}" }
        }
    }

    private fun NoticeFileDto.toAttachmentRow(): AttachmentRow {
        return AttachmentRow(
            fileName = fileName,
            registeredDateText = registeredAt?.toDisplayDate()?.let { "$it 등록" } ?: "등록일 미상",
            fileUrl = fileUrl,
            fileType = fileType
        )
    }

    private fun formatAgeRange(minAge: Int?, maxAge: Int?): String {
        return when {
            minAge == null && maxAge == null -> FALLBACK_TEXT
            maxAge == null -> "만 ${minAge}세 이상"
            minAge == null -> "만 ${maxAge}세 이하"
            else -> "만 ${minAge}세 ~ ${maxAge}세"
        }
    }

    private fun formatWon(won: Long): String {
        val manwon = won / 10_000
        val eok = manwon / 10_000
        val remainingManwon = manwon % 10_000
        return when {
            eok == 0L -> String.format(Locale.KOREA, "%,d만 원", manwon)
            remainingManwon == 0L -> "${eok}억 원"
            else -> String.format(Locale.KOREA, "%d억 %,d만 원", eok, remainingManwon)
        }
    }

    private fun formatWonRange(minWon: Long?, maxWon: Long?): String {
        val minText = minWon?.let { formatWon(it) }
        val maxText = maxWon?.let { formatWon(it) }
        return when {
            minText == null && maxText == null -> FALLBACK_TEXT
            minWon == maxWon -> minText ?: maxText ?: FALLBACK_TEXT
            else -> listOfNotNull(minText, maxText).joinToString(" ~ ")
        }
    }

    private fun String.toTargetTypeLabel(): String = when (this) {
        "YOUTH" -> "청년"
        "NEWLYWED" -> "신혼부부"
        "COMMON" -> "공통"
        else -> "기타"
    }

    private fun String?.toDisplayDate(): String {
        if (this == null) return FALLBACK_TEXT
        return runCatching { Instant.parse(this).atZone(DISPLAY_ZONE).format(DISPLAY_DATE_FORMATTER) }
            .getOrDefault(FALLBACK_TEXT)
    }
}
