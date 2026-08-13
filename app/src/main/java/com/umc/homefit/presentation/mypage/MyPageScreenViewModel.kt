package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.auth.AuthRepository
import com.umc.homefit.domain.repository.mypage.MyPageRepository
import com.umc.homefit.util.error.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@HiltViewModel
class MyPageScreenViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageScreenUiState>(MyPageScreenUiState.Loading)
    val uiState: StateFlow<MyPageScreenUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = MyPageScreenUiState.Loading

            val (profileResult, basicInfoResult) = coroutineScope {
                val profileDeferred = async { myPageRepository.getProfile() }
                val basicInfoDeferred = async { myPageRepository.getBasicInfo() }
                profileDeferred.await() to basicInfoDeferred.await()
            }

            if (basicInfoResult !is NetworkResult.Success) {
                _uiState.value = MyPageScreenUiState.Error(
                    (basicInfoResult as? NetworkResult.Error)?.message ?: "프로필 정보를 불러오지 못했습니다"
                )
                return@launch
            }

            val nickname = when {
                profileResult is NetworkResult.Success ->
                    profileResult.data.nickname ?: "닉네임을 설정해주세요"
                profileResult is NetworkResult.Error && profileResult.errorCode == ErrorCode.COMMON404 ->
                    "닉네임을 설정해주세요"
                else -> null
            }

            if (nickname == null) {
                _uiState.value = MyPageScreenUiState.Error(
                    (profileResult as? NetworkResult.Error)?.message ?: "프로필 정보를 불러오지 못했습니다"
                )
                return@launch
            }

            _uiState.value = MyPageScreenUiState.Success(
                MyPageProfile(
                    nickname = nickname,
                    email = basicInfoResult.data.email,
                    profileImageUrl = (profileResult as? NetworkResult.Success)?.data?.profileImageUrl
                )
            )
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onComplete()
        }
    }
}
