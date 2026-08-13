package com.umc.homefit.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginScreenUiState {
    object Idle : LoginScreenUiState
    data class Loading(val provider: String) : LoginScreenUiState
    object Success : LoginScreenUiState
    data class Error(val message: String) : LoginScreenUiState
}

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginScreenUiState>(LoginScreenUiState.Idle)
    val uiState: StateFlow<LoginScreenUiState> = _uiState.asStateFlow()

    fun socialLogin(provider: String, oauthToken: String) {
        viewModelScope.launch {
            _uiState.value = LoginScreenUiState.Loading(provider)
            when (val result = authRepository.socialLogin(provider, oauthToken)) {
                is NetworkResult.Success -> _uiState.value = LoginScreenUiState.Success
                is NetworkResult.Error -> _uiState.value = LoginScreenUiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginScreenUiState.Idle
    }
}
