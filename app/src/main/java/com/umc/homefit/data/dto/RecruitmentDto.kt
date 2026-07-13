package com.umc.homefit.data.dto

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
    val deposit: Long,
    val monthlyRent: Long,
    val announcementDate: String,
    val announcementNumber: String,
    val area: Double,
    val applicationStartDate: String,
    val applicationEndDate: String,
    val status: RecruitmentStatus,
    val competitionRate: String,
    val isBookmarked: Boolean = false
)
