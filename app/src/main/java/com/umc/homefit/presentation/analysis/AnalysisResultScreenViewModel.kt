package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalysisResultScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<AnalysisResultScreenUiState>(AnalysisResultScreenUiState.Loading)
    val uiState: StateFlow<AnalysisResultScreenUiState> = _uiState.asStateFlow()

    init {
        loadAnalysisResult()
    }

    private fun loadAnalysisResult() {
        // 실제 API 연동 시 이곳에서 Repository를 호출하여 데이터를 받아올 것
        // 현재는 기획서 시안 기반의 초기 성공 데이터를 세팅해 둔 상태
        _uiState.value = AnalysisResultScreenUiState.Success(
            data = AnalysisResultData(
                probabilityGrade = "높음",
                percentileText = "상위 20~30%",
                score = 72,
                expectedDeposit = "3,200만 원",
                expectedMonthlyRent = "42만 원",
                infoTags = listOf("전용 36m² · 2순위 기준 추정", "보증금 1,000만 원 전환 기준"),
                criteriaStatus = listOf(
                    CriteriaItem("소득 기준", "적합", true),
                    CriteriaItem("자산 기준", "적합", true),
                    CriteriaItem("거주 지역", "적합", true),
                    CriteriaItem("청약 자격", "확인 필요", false)
                )
            )
        )
    }
}
