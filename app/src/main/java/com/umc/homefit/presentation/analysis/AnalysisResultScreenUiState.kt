package com.umc.homefit.presentation.analysis

sealed interface AnalysisResultScreenUiState {
    object Loading : AnalysisResultScreenUiState
    data class Error(val message: String) : AnalysisResultScreenUiState
    data class Success(val data: AnalysisResultData) : AnalysisResultScreenUiState // String에서 상세 모델로 변경
}

data class AnalysisResultData(
    val probabilityGrade: String,      // 입주 가능성 등급 ("높음", "낮음" 등)
    val percentileText: String,        // 상위 백분위 정보 ("상위 20~30%") — API 미지원, #72 문의 1 답변 전까지 빈 문자열
    val score: Int,                    // 입주 분석 점수 (72)
    val expectedDeposit: String,       // 예상 보증금 ("3,200만 원")
    val expectedMonthlyRent: String,   // 예상 월세 ("42만 원")
    val criteriaStatus: List<CriteriaItem>, // 조건별 충족 현황 리스트
    val inputInfoRows: List<InfoRowItem> = emptyList() // 아코디언 "입력 정보" — conditionResults의 userValue 기반
)

data class CriteriaItem(
    val title: String,                 // 조건명 ("소득 기준", "자산 기준" 등)
    val statusText: String,            // 상태 텍스트 ("적합", "확인 필요")
    val isSuitable: Boolean            // UI 색상 분기용 플래그 (true: 초록, false: 주황)
)

data class InfoRowItem(
    val title: String,                 // 서버가 내려주는 conditionName ("소득 조건" 등)
    val value: String                  // 서버가 내려주는 userValue ("월소득 280만원" 등)
)
