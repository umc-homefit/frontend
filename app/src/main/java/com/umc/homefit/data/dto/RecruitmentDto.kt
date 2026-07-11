package com.umc.homefit.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecruitmentDto(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val rentType: String,
    val deposit: Long,
    val monthlyRent: Long,
    val announcementDate: String
)
