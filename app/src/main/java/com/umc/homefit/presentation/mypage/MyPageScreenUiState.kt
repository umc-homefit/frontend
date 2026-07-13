package com.umc.homefit.presentation.mypage

sealed interface MyPageScreenUiState {
    object Loading : MyPageScreenUiState
    data class Success(val profile: MyPageProfile) : MyPageScreenUiState
    data class Error(val message: String) : MyPageScreenUiState
}

data class MyPageProfile(
    val nickname: String,
    val email: String,
    val profileImageUrl: String? = null
)
