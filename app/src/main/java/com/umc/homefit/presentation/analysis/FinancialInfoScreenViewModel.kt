package com.umc.homefit.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.HousingOwnershipStatus
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequest
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.ConditionProfileRepository
import com.umc.homefit.util.calculateIsHomeless
import com.umc.homefit.util.error.ErrorCode
import com.umc.homefit.util.toAnnualIncomeText
import com.umc.homefit.util.toApiAmount
import com.umc.homefit.util.toDisplayAmount
import com.umc.homefit.util.toMonthlyIncomeAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialInfoScreenViewModel @Inject constructor(
    private val conditionProfileRepository: ConditionProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FinancialInfoScreenUiState>(
        FinancialInfoScreenUiState.Loading
    )
    val uiState: StateFlow<FinancialInfoScreenUiState> = _uiState.asStateFlow()

    init {
        loadConditionProfile()
    }

    fun loadConditionProfile() {
        _uiState.value = FinancialInfoScreenUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = conditionProfileRepository.getConditionProfile()) {
                is NetworkResult.Success -> FinancialInfoScreenUiState.Success(
                    draft = result.data.toDraft()
                )
                is NetworkResult.Error -> {
                    if (result.errorCode == ErrorCode.FINANCE404 || result.errorCode == ErrorCode.COMMON404) {
                        FinancialInfoScreenUiState.Success()
                    } else {
                        FinancialInfoScreenUiState.Error(
                            message = result.message,
                            draft = ConditionProfileDraft()
                        )
                    }
                }
            }
        }
    }

    private fun ConditionProfileResponse.toDraft(): ConditionProfileDraft = ConditionProfileDraft(
        annualIncomeText = toAnnualIncomeText(monthlyIncomeAmount),
        totalAssetText = toDisplayAmount(totalAssetAmount),
        financialAssetText = toDisplayAmount(cashSavings),
        totalDebtText = toDisplayAmount(totalDebtAmount),
        monthlyRepaymentText = toDisplayAmount(monthlyDebtPaymentAmount),
        housingStatus = housingOwnershipStatus.name
    )

    fun onIncomeNext(annualIncomeText: String) {
        updateDraft { it.copy(annualIncomeText = annualIncomeText) }
    }

    fun onAssetNext(totalAssetText: String, financialAssetText: String) {
        updateDraft {
            it.copy(totalAssetText = totalAssetText, financialAssetText = financialAssetText)
        }
    }

    fun onDebtNext(totalDebtText: String, monthlyRepaymentText: String) {
        updateDraft {
            it.copy(totalDebtText = totalDebtText, monthlyRepaymentText = monthlyRepaymentText)
        }
    }

    fun onHouseNextAndSubmit(housingStatus: String) {
        updateDraftAndSubmit { it.copy(housingStatus = housingStatus) }
    }

    fun onIncomeEditSave(annualIncomeText: String) {
        updateDraftAndSubmit { it.copy(annualIncomeText = annualIncomeText) }
    }

    fun onAssetEditSave(totalAssetText: String, financialAssetText: String) {
        updateDraftAndSubmit {
            it.copy(totalAssetText = totalAssetText, financialAssetText = financialAssetText)
        }
    }

    fun onDebtEditSave(totalDebtText: String, monthlyRepaymentText: String) {
        updateDraftAndSubmit {
            it.copy(totalDebtText = totalDebtText, monthlyRepaymentText = monthlyRepaymentText)
        }
    }

    /**
     * draft를 transform으로 갱신한 뒤 UpdateConditionProfileRequest를 조립해 PUT을 호출
     * 성공하면 isSubmitted = true (화면에서 COMPLETE 이동 또는 자동으로 뒤로가기 처리)
     * 실패하면 Error 상태로 전환 (draft는 보존해서 재입력 없이 복구 가능)
     */
    private fun updateDraftAndSubmit(transform: (ConditionProfileDraft) -> ConditionProfileDraft) {
        val draft = transform(currentDraft())
        _uiState.value = FinancialInfoScreenUiState.Success(draft = draft, isSubmitting = true)

        val request = buildRequest(draft)
        if (request == null) {
            _uiState.value = FinancialInfoScreenUiState.Error(
                message = "주택 소유 상태 값을 확인할 수 없습니다",
                draft = draft
            )
            return
        }

        viewModelScope.launch {
            when (val result = conditionProfileRepository.updateConditionProfile(request)) {
                is NetworkResult.Success -> {
                    _uiState.value = FinancialInfoScreenUiState.Success(
                        draft = draft,
                        isSubmitting = false,
                        isSubmitted = true
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = FinancialInfoScreenUiState.Error(
                        message = result.message,
                        draft = draft
                    )
                }
            }
        }
    }

    private fun buildRequest(draft: ConditionProfileDraft): UpdateConditionProfileRequest? {
        val housingStatusText = draft.housingStatus ?: return null
        val housingOwnershipStatus = runCatching {
            HousingOwnershipStatus.valueOf(housingStatusText)
        }.getOrNull() ?: return null

        return UpdateConditionProfileRequest(
            monthlyIncomeAmount = toMonthlyIncomeAmount(draft.annualIncomeText),
            totalAssetAmount = toApiAmount(draft.totalAssetText),
            totalDebtAmount = toApiAmount(draft.totalDebtText),
            monthlyDebtPaymentAmount = toApiAmount(draft.monthlyRepaymentText),
            cashSavings = toApiAmount(draft.financialAssetText),
            isHomeless = calculateIsHomeless(housingStatusText),
            housingOwnershipStatus = housingOwnershipStatus
        )
    }

    private fun currentDraft(): ConditionProfileDraft = when (val state = _uiState.value) {
        is FinancialInfoScreenUiState.Success -> state.draft
        is FinancialInfoScreenUiState.Error -> state.draft
        is FinancialInfoScreenUiState.Loading -> ConditionProfileDraft()
    }

    private fun updateDraft(transform: (ConditionProfileDraft) -> ConditionProfileDraft) {
        _uiState.value = FinancialInfoScreenUiState.Success(draft = transform(currentDraft()))
    }
}
