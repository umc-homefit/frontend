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

    /**
     * 기존 금융 정보 프로필을 조회해 draft를 채운다. (화면 진입 시 자동 호출)
     *
     * 실패 응답의 errorCode로 "진짜 없음"과 "진짜 오류"를 구분한다 (공통 오류 코드 규격 기준):
     * - FINANCE404("등록된 금융 조건 정보 없음") 또는 COMMON404: 아직 프로필을 등록한 적 없는
     *   신규 사용자 -> 정상, 빈 draft(신규 입력 상태)로 시작한다.
     *   (공통 오류 코드 규격상 도메인별 코드가 없으면 COMMON404로 내려올 수 있고, 이 엔드포인트에서
     *   404가 나는 이유는 사실상 "이 사용자 프로필이 없음" 하나뿐이라 둘 다 같은 케이스로 취급한다)
     * - 그 외(AUTH401, COMMON500, UNKNOWN 등): 진짜 오류 -> Error 상태로 전환한다.
     */
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

    /** INCOME 스텝 완료: 연간 총소득 입력값을 draft에 누적한다. */
    fun onIncomeNext(annualIncomeText: String) {
        updateDraft { it.copy(annualIncomeText = annualIncomeText) }
    }

    /** ASSET 스텝 완료: 총 보유 자산 / 금융 자산 입력값을 draft에 누적한다. */
    fun onAssetNext(totalAssetText: String, financialAssetText: String) {
        updateDraft {
            it.copy(totalAssetText = totalAssetText, financialAssetText = financialAssetText)
        }
    }

    /** DEBT 스텝 완료: 총 부채 금액 / 월 상환액 입력값을 draft에 누적한다. */
    fun onDebtNext(totalDebtText: String, monthlyRepaymentText: String) {
        updateDraft {
            it.copy(totalDebtText = totalDebtText, monthlyRepaymentText = monthlyRepaymentText)
        }
    }

    /**
     * HOUSE 스텝 완료: 그동안 누적된 draft + 이번에 선택된 housingStatus로 PUT을 호출한다.
     * (마법사 플로우의 마지막 스텝이자, 수정 화면에서 주택 정보만 고칠 때도 재사용)
     */
    fun onHouseNextAndSubmit(housingStatus: String) {
        updateDraftAndSubmit { it.copy(housingStatus = housingStatus) }
    }

    /** 수정 화면(FinancialInfoEditScreen)에서 소득 항목만 고쳐서 저장할 때 쓴다. */
    fun onIncomeEditSave(annualIncomeText: String) {
        updateDraftAndSubmit { it.copy(annualIncomeText = annualIncomeText) }
    }

    /** 수정 화면에서 자산 항목만 고쳐서 저장할 때 쓴다. */
    fun onAssetEditSave(totalAssetText: String, financialAssetText: String) {
        updateDraftAndSubmit {
            it.copy(totalAssetText = totalAssetText, financialAssetText = financialAssetText)
        }
    }

    /** 수정 화면에서 부채 항목만 고쳐서 저장할 때 쓴다. */
    fun onDebtEditSave(totalDebtText: String, monthlyRepaymentText: String) {
        updateDraftAndSubmit {
            it.copy(totalDebtText = totalDebtText, monthlyRepaymentText = monthlyRepaymentText)
        }
    }

    /**
     * draft를 transform으로 갱신한 뒤 UpdateConditionProfileRequest를 조립해 PUT을 호출한다.
     * 성공하면 isSubmitted = true (화면에서 COMPLETE 이동 또는 자동으로 뒤로가기 처리),
     * 실패하면 Error 상태로 전환한다 (draft는 보존해서 재입력 없이 복구 가능).
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
