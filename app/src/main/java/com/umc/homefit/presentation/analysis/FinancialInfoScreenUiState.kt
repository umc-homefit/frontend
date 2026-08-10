package com.umc.homefit.presentation.analysis

data class ConditionProfileDraft(
    val annualIncomeText: String = "",
    val totalAssetText: String = "",
    val financialAssetText: String = "",
    val totalDebtText: String = "",
    val monthlyRepaymentText: String = "",
    val housingStatus: String? = null
)

sealed interface FinancialInfoScreenUiState {
    object Loading : FinancialInfoScreenUiState

    data class Success(
        val draft: ConditionProfileDraft = ConditionProfileDraft(),
        val isSubmitting: Boolean = false,
        val isSubmitted: Boolean = false,
        // noticeId/unitId를 가지고 진입한 경우(공고 상세에서 "분석 요청하기")에만 채워짐.
        // null이면 공고와 무관하게 재무 프로필만 저장하는 흐름이라 분석 결과 화면으로 이동하지 않음.
        val analysisId: String? = null
    ) : FinancialInfoScreenUiState

    data class Error(
        val message: String,
        val draft: ConditionProfileDraft
    ) : FinancialInfoScreenUiState
}
