package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.repository.RecruitmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitmentListScreenViewModel @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecruitmentListScreenUiState>(RecruitmentListScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentListScreenUiState> = _uiState.asStateFlow()

    init {
        loadRecruitments()
    }

    private fun loadRecruitments() {
        viewModelScope.launch {
            _uiState.value = try {
                RecruitmentListScreenUiState.Success(recruitmentRepository.getRecruitments())
            } catch (e: Exception) {
                RecruitmentListScreenUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    fun toggleBookmark(id: String) {
        val currentState = _uiState.value
        if (currentState is RecruitmentListScreenUiState.Success) {
            _uiState.value = currentState.copy(
                recruitments = currentState.recruitments.map { recruitment ->
                    if (recruitment.id == id) {
                        recruitment.copy(isBookmarked = !recruitment.isBookmarked)
                    } else {
                        recruitment
                    }
                }
            )
        }
    }
}
