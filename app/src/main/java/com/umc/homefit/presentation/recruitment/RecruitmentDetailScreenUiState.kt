package com.umc.homefit.presentation.recruitment

sealed interface RecruitmentDetailScreenUiState {
    object Loading : RecruitmentDetailScreenUiState
    data class Success(val recruitment: RecruitmentDetailUiModel) : RecruitmentDetailScreenUiState
    data class Error(val message: String) : RecruitmentDetailScreenUiState
}

data class RecruitmentDetailUiModel(
    val noticeId: Long,
    val title: String,
    val status: String,
    val statusDisplayText: String,
    val isSaved: Boolean,
    // 공급 정보
    val supplyLocation: String,
    val supplyType: String,
    val unitSummary: String,
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
    val registeredDateText: String
)
