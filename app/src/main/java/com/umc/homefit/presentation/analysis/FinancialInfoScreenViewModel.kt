package com.umc.homefit.presentation.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.analysis.ConditionProfileResponse
import com.umc.homefit.data.dto.analysis.HousingOwnershipStatus
import com.umc.homefit.data.dto.analysis.UpdateConditionProfileRequest
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
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
    private val conditionProfileRepository: ConditionProfileRepository,
    private val analysisRepository: AnalysisRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // noticeId/unitId 중 하나만 있는 "반쪽" 상태가 여기저기서 null 체크로 새는 걸 막기 위해
    // 진입 시점에 한 번만 정리해서 EntryContext로 고정한다.
    private val entryContext: EntryContext = run {
        val id = savedStateHandle.get<Long>("noticeId")
        val unit = savedStateHandle.get<Long>("unitId")
        if (id != null && unit != null) {
            EntryContext.ForAnalysis(noticeId = id, unitId = unit)
        } else {
            EntryContext.ProfileOnly
        }
    }

    // 공고 상세에서 "분석 요청하기"로 진입했는지(ForAnalysis), Finance 탭 등에서 프로필만 입력하러 왔는지(ProfileOnly)
    private sealed interface EntryContext {
        data class ForAnalysis(val noticeId: Long, val unitId: Long) : EntryContext
        data object ProfileOnly : EntryContext
    }

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
                    when (val context = entryContext) {
                        is EntryContext.ForAnalysis -> {
                            requestAnalysisAndComplete(draft, context.noticeId, context.unitId)
                        }
                        EntryContext.ProfileOnly -> {
                            // 공고와 무관하게 재무 프로필만 저장하는 흐름: 분석을 만들지 않고 완료 처리
                            _uiState.value = FinancialInfoScreenUiState.Success(
                                draft = draft,
                                isSubmitting = false,
                                isSubmitted = true
                            )
                        }
                    }
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

    // 완료 화면의 "다시 시도"에서 호출. ForAnalysis로 들어온 경우에만 의미가 있음
    fun retryAnalysisRequest() {
        val context = entryContext as? EntryContext.ForAnalysis ?: return
        val draft = currentDraft()
        viewModelScope.launch {
            requestAnalysisAndComplete(draft, context.noticeId, context.unitId)
        }
    }

    private suspend fun requestAnalysisAndComplete(draft: ConditionProfileDraft, noticeId: Long, unitId: Long) {
        // 진행 중 표시: 이전에 실패 메시지가 남아있었다면 재시도 중에는 지워서 보여줌
        _uiState.value = FinancialInfoScreenUiState.Success(draft = draft, isSubmitting = true)
        when (val result = analysisRepository.requestEligibilityAnalysis(noticeId, unitId)) {
            is NetworkResult.Success -> {
                _uiState.value = FinancialInfoScreenUiState.Success(
                    draft = draft,
                    isSubmitting = false,
                    isSubmitted = true,
                    analysisId = result.data.analysisId.toString()
                )
            }
            is NetworkResult.Error -> {
                // 재무 프로필 저장은 이미 성공했으므로 Error 화면으로 내리지 않고,
                // 완료 단계에서 실패 메시지 + 재시도로 안내 (뒤로가기가 먹통이 되는 문제 방지)
                _uiState.value = FinancialInfoScreenUiState.Success(
                    draft = draft,
                    isSubmitting = false,
                    isSubmitted = true,
                    analysisFailedMessage = result.message
                )
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
