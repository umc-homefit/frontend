package com.umc.homefit.presentation.recruitment

import com.umc.homefit.util.error.ErrorCode

sealed interface RecruitmentDetailScreenUiState {
    object Loading : RecruitmentDetailScreenUiState
    data class Success(val recruitment: RecruitmentDetailUiModel) : RecruitmentDetailScreenUiState
    data class Error(val message: String, val errorCode: ErrorCode) : RecruitmentDetailScreenUiState
}

data class RecruitmentDetailUiModel(
    val noticeId: Long,
    val title: String,
    val status: String,
    val statusDisplayText: String,
    val isSaved: Boolean,
    val sourceUrl: String,
    // targetType 한글 라벨(청년/신혼부부/공통), "기타"인 경우 null로 내려와 뱃지 미표시
    val targetTypeBadgeText: String?,
    // 공급 정보
    val supplyLocation: String,
    val supplyType: String,
    val unitSummary: String,
    // 입주 분석 요청 시 사용할 유닛 ID. 유닛이 여러 개인 공고는 우선 첫 번째 유닛으로 분석 (유닛 선택 UI는 별도 후속 작업)
    val primaryUnitId: Long?,
    val depositRangeText: String,
    val monthlyRentRangeText: String,
    val moveInDate: String,
    // 자격 조건
    val ageRange: String,
    val incomeStandard: String,
    val assetStandard: String,
    val housingOwnership: String,
    val residencyRequirement: String,
    // 신청 기간
    val applicationStartText: String,
    val applicationEndText: String,
    val winnerAnnouncementDate: String,
    val contractPeriod: String,
    val attachments: List<AttachmentRow>,
    val photoUrls: List<String>
)

data class AttachmentRow(
    val fileName: String,
    val registeredDateText: String,
    val fileUrl: String,
    val fileType: String
)
