package com.umc.homefit.presentation.analysis

sealed interface AnalysisResultScreenUiState {
    object Loading : AnalysisResultScreenUiState
    data class Error(val message: String) : AnalysisResultScreenUiState
    data class Success(val data: AnalysisResultData) : AnalysisResultScreenUiState // String에서 상세 모델로 변경
}

data class AnalysisResultData(
    val probabilityGrade: String,      // 입주 가능성 등급 ("높음", "낮음" 등)
    val percentileText: String,        // 상위 백분위 정보 ("상위 20~30%")
    val score: Int,                    // 입주 분석 점수 (72)
    val expectedDeposit: String,       // 예상 보증금 ("3,200만 원")
    val expectedMonthlyRent: String,   // 예상 월세 ("42만 원")
    val infoTags: List<String>,        // 하단 기준 안내 태그 목록
    val criteriaStatus: List<CriteriaItem> // 조건별 충족 현황 리스트
)

data class CriteriaItem(
    val title: String,                 // 조건명 ("소득 기준", "자산 기준" 등)
    val statusText: String,            // 상태 텍스트 ("적합", "확인 필요")
    val isSuitable: Boolean            // UI 색상 분기용 플래그 (true: 초록, false: 주황)
)
