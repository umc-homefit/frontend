package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.mypage.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageScreenViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageScreenUiState>(MyPageScreenUiState.Loading)
    val uiState: StateFlow<MyPageScreenUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = MyPageScreenUiState.Loading

            val profileResult = myPageRepository.getProfile()
            val basicInfoResult = myPageRepository.getBasicInfo()

            if (profileResult is NetworkResult.Success && basicInfoResult is NetworkResult.Success) {
                _uiState.value = MyPageScreenUiState.Success(
                    MyPageProfile(
                        nickname = profileResult.data.nickname,
                        email = basicInfoResult.data.email,
                        profileImageUrl = profileResult.data.profileImageUrl
                    )
                )
            } else {
                val errorMessage = (profileResult as? NetworkResult.Error)?.message
                    ?: (basicInfoResult as? NetworkResult.Error)?.message
                    ?: "프로필 정보를 불러오지 못했습니다"
                _uiState.value = MyPageScreenUiState.Error(errorMessage)
            }
        }
    }
}
