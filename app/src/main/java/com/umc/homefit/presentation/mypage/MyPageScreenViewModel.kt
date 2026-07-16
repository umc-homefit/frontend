package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageScreenUiState>(MyPageScreenUiState.Loading)
    val uiState: StateFlow<MyPageScreenUiState> = _uiState.asStateFlow()

    init {
        // TODO: 실제 사용자 프로필 API 연동
        _uiState.value = MyPageScreenUiState.Success(
            MyPageProfile(
                nickname = "홍길동",
                email = "honggildong@email.com"
            )
        )
    }
}

