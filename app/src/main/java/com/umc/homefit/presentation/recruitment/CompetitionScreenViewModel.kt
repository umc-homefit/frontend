package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.home.CompetitionDto
import com.umc.homefit.data.dto.home.CompetitionHistoryEntry
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import com.umc.homefit.data.dto.recruitment.RecruitmentStatus
import com.umc.homefit.data.dto.home.TypeCompetitionRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noticeId: String = checkNotNull(savedStateHandle["recruitmentId"])

    private val _uiState = MutableStateFlow<CompetitionScreenUiState>(CompetitionScreenUiState.Loading)
    val uiState: StateFlow<CompetitionScreenUiState> = _uiState.asStateFlow()

    init {
        loadCompetition()
    }

    private fun loadCompetition() {
        // TODO: API 연동 시 repository를 통해 noticeId 기준으로 실제 경쟁률 데이터를 조회하도록 교체
        viewModelScope.launch {
            _uiState.value = CompetitionScreenUiState.Success(
                recruitment = mockRecruitment(noticeId),
                competition = mockCompetition(noticeId)
            )
        }
    }

    fun toggleBookmark() {
        val currentState = _uiState.value
        if (currentState is CompetitionScreenUiState.Success) {
            _uiState.value = currentState.copy(
                recruitment = currentState.recruitment.copy(isBookmarked = !currentState.recruitment.isBookmarked)
            )
        }
    }

    private fun mockRecruitment(id: String) = RecruitmentDto(
        id = id,
        title = "강동구 청년안심주택 2025-03호",
        company = "한국토지주택공사",
        location = "서울 강동구 천호동 123-4",
        rentType = "청년안심주택 (임대)",
        depositMin = 32000000,
        depositMax = 48000000,
        monthlyRentMin = 280000,
        monthlyRentMax = 410000,
        announcementDate = "2026-07-13",
        announcementNumber = "2026-강남-001",
        area = 39.87,
        applicationStartDate = "2025년 6월 9일 (월) 오전 10:00",
        applicationEndDate = "2025년 6월 13일 (금) 오후 6:00",
        status = RecruitmentStatus.RECRUITING,
        competitionRate = "47.3:1",
        isBookmarked = true,
        tags = listOf("청년우선공급", "역세권")
    )

    private fun mockCompetition(noticeId: String) = CompetitionDto(
        noticeId = noticeId,
        finalRate = "47.3:1",
        finalRateBaseDate = "2025.06.02 기준 최종 집계",
        expectedScore = 60,
        expectedScoreLabel = "60점 이상",
        totalUnits = 120,
        totalApplicants = 5676,
        firstPriorityRate = "38.2:1",
        secondPriorityRate = "9.1:1",
        specialSupplyRate = "22.7:1",
        generalSupplyRate = "61.4:1",
        applicationPeriod = "2025.05.28 ~ 2025.06.01",
        typeRates = listOf(
            TypeCompetitionRate(unitType = "16A", supplyUnits = 20, applicantCount = 964, rate = "48.2:1"),
            TypeCompetitionRate(unitType = "24A", supplyUnits = 32, applicantCount = 1856, rate = "58.0:1"),
            TypeCompetitionRate(unitType = "36A", supplyUnits = 26, applicantCount = 1016, rate = "39.1:1"),
            TypeCompetitionRate(unitType = "39A", supplyUnits = 18, applicantCount = 612, rate = "34.0:1")
        ),
        history = listOf(
            CompetitionHistoryEntry(roundLabel = "23년 1차", supplyUnits = 110, applicantCount = 3124, rate = 28.4),
            CompetitionHistoryEntry(roundLabel = "23년 2차", supplyUnits = 109, applicantCount = 3568, rate = 32.7),
            CompetitionHistoryEntry(roundLabel = "24년 1차", supplyUnits = 113, applicantCount = 4689, rate = 41.5),
            CompetitionHistoryEntry(roundLabel = "24년 2차", supplyUnits = 114, applicantCount = 5289, rate = 46.2),
            CompetitionHistoryEntry(roundLabel = "25년 1차", supplyUnits = 120, applicantCount = 5676, rate = 47.3)
        )
    )
}
