package com.umc.homefit.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.auth.AuthRepository
import com.umc.homefit.util.error.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SignUpScreenUiState {
    object Idle : SignUpScreenUiState
    object Loading : SignUpScreenUiState
    object Success : SignUpScreenUiState
    object EmailDuplicateError : SignUpScreenUiState
    data class Error(val message: String) : SignUpScreenUiState
}

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<SignUpScreenUiState>(SignUpScreenUiState.Idle)
    val uiState: StateFlow<SignUpScreenUiState> = _uiState.asStateFlow()

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = SignUpScreenUiState.Loading
            when (val result = authRepository.signup(email, password)) {
                is NetworkResult.Success -> {
                    _uiState.value = SignUpScreenUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = if (result.errorCode == ErrorCode.AUTH409) {
                        SignUpScreenUiState.EmailDuplicateError
                    } else {
                        SignUpScreenUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = SignUpScreenUiState.Idle
    }
}
