package com.umc.homefit.data.dto

data class CompetitionDto(
    val noticeId: String,
    val finalRate: String,              // "47.3:1"
    val finalRateBaseDate: String,      // "2025.06.02 기준 최종 집계"
    val expectedScore: Int,             // 60
    val expectedScoreLabel: String,     // "60점 이상"
    val totalUnits: Int,                // 120
    val totalApplicants: Int,           // 5676
    val firstPriorityRate: String,      // "38.2:1"
    val secondPriorityRate: String,     // "9.1:1"
    val specialSupplyRate: String,      // "22.7:1"
    val generalSupplyRate: String,      // "61.4:1"
    val applicationPeriod: String,      // "2025.05.28 ~ 2025.06.01"
    val typeRates: List<TypeCompetitionRate>,
    val history: List<CompetitionHistoryEntry>
)

data class TypeCompetitionRate(
    val unitType: String,       // "16A"
    val supplyUnits: Int,       // 20
    val applicantCount: Int,    // 964
    val rate: String            // "48.2:1"
)

data class CompetitionHistoryEntry(
    val roundLabel: String,     // "23년 1차"
    val supplyUnits: Int,       // 110
    val applicantCount: Int,    // 3124
    val rate: Double            // 28.4 — 차트 y값 계산용 숫자 타입
)
