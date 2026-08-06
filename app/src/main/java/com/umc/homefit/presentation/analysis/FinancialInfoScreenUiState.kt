package com.umc.homefit.presentation.analysis

/**
 * INCOME/ASSET/DEBT/HOUSE 스텝에서 입력받은 값을 누적 보관하는 draft.
 * HOUSE 스텝 완료 시 이 값들을 FinancialInfoMapper의 함수들로 변환해
 * UpdateConditionProfileRequest를 조립하는 데 쓰인다.
 * (금액 필드는 아직 "만 원" 단위 UI 원문 텍스트 그대로 보관 — 변환은 제출 시점에 수행)
 */
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
        // true가 되면 화면(FinancialInfoScreen)이 COMPLETE 스텝으로 이동시킨다.
        val isSubmitted: Boolean = false
    ) : FinancialInfoScreenUiState

    data class Error(
        val message: String,
        // 실패 시에도 그동안 입력한 값을 잃지 않도록 draft를 함께 들고 있는다.
        val draft: ConditionProfileDraft
    ) : FinancialInfoScreenUiState
}
