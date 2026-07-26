package com.umc.homefit.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SplashState {
    object Loading : SplashState
    object Authenticated : SplashState   // 토큰 유효함 -> 메인으로
    object Unauthenticated : SplashState // 토큰 없거나 만료됨 -> 로그인으로
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    // private val tokenRepository: TokenRepository // 토큰 관리 리포지토리 주입
) : ViewModel() {

    private val _loginState = MutableStateFlow<SplashState>(SplashState.Loading)
    val loginState: StateFlow<SplashState> = _loginState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // 예시: 토큰 존재 여부 확인 (API/로컬 DB 조회)
            // val hasToken = tokenRepository.hasValidToken()
            val hasToken = false // 임시 값 (API 연결 전)

            if (hasToken) {
                _loginState.value = SplashState.Authenticated
            } else {
                _loginState.value = SplashState.Unauthenticated
            }
        }
    }
}
