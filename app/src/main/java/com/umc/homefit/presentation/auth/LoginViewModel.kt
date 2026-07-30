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

enum class LoginStep {
    EMAIL,
    PASSWORD,
    COMPLETE
}

private val LOGIN_EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

data class LoginUiState(
    val step: LoginStep = LoginStep.EMAIL,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmailValid: Boolean
        get() = LOGIN_EMAIL_REGEX.matches(email)

    val isPasswordValid: Boolean
        get() = password.isNotEmpty()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onEmailNext() {
        _uiState.value = _uiState.value.copy(step = LoginStep.PASSWORD)
    }

    fun onPasswordNext() {
        val current = _uiState.value
        if (current.isLoading) return

        viewModelScope.launch {
            _uiState.value = current.copy(isLoading = true, errorMessage = null)

            when (val result = authRepository.login(current.email, current.password)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, step = LoginStep.COMPLETE)
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    fun onStepBack() {
        val current = _uiState.value
        if (current.step == LoginStep.PASSWORD) {
            _uiState.value = current.copy(step = LoginStep.EMAIL, errorMessage = null)
        }
    }
}
