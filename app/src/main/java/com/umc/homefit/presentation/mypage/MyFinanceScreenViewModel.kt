package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyFinanceScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<MyFinanceScreenUiState>(MyFinanceScreenUiState.Loading)
    val uiState: StateFlow<MyFinanceScreenUiState> = _uiState.asStateFlow()

    init {
        // TODO: 실제 금융 정보 API 연동
        _uiState.value = MyFinanceScreenUiState.Success(
            sections = listOf(
                FinanceInfoSection(
                    title = "소득 정보",
                    rows = listOf(
                        FinanceInfoRow("연간 총소득", "4,800만 원"),
                        FinanceInfoRow("소득 유형", "근로소득")
                    )
                ),
                FinanceInfoSection(
                    title = "자산 정보",
                    rows = listOf(
                        FinanceInfoRow("총 보유 자산", "6,500만 원"),
                        FinanceInfoRow("금융 자산", "2,800만 원")
                    )
                ),
                FinanceInfoSection(
                    title = "부채 정보",
                    rows = listOf(
                        FinanceInfoRow("총 부채 금액", "1,800만 원"),
                        FinanceInfoRow("월 상환액", "35만 원")
                    )
                ),
                FinanceInfoSection(
                    title = "주택 보유 여부",
                    rows = listOf(
                        FinanceInfoRow("본인 무주택")
                    )
                )
            )
        )
    }
}
