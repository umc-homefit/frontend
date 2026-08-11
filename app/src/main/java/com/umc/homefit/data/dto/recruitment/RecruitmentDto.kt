package com.umc.homefit.data.dto.recruitment

import kotlinx.serialization.Serializable

@Serializable
enum class RecruitmentStatus {
    RECRUITING, SCHEDULED, CLOSING_SOON
}

@Serializable
data class RecruitmentDto(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val rentType: String,
    val depositMin: Long,
    val depositMax: Long,
    val monthlyRentMin: Long,
    val monthlyRentMax: Long,
    val announcementDate: String,
    val announcementNumber: String,
    val area: Double,
    val applicationStartDate: String,
    val applicationEndDate: String,
    val status: RecruitmentStatus,
    val competitionRate: String,
    val isBookmarked: Boolean = false,
    val tags: List<String> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val moveInDate: String = "공고문 참고",
    val ageRange: String = "공고문 참고",
    val incomeStandard: String = "공고문 참고",
    val assetStandard: String = "공고문 참고",
    val housingOwnership: String = "공고문 참고",
    val residencyRequirement: String = "공고문 참고",
    val winnerAnnouncementDate: String = "공고문 참고",
    val contractPeriod: String = "공고문 참고",
    val unitSummary: String = "공고문 참고",
    val photoResIds: List<Int> = emptyList()
)

@Serializable
data class Attachment(
    val fileName: String,
    val registeredDate: String
)
