package com.umc.homefit.presentation.recruitment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitmentDetailScreenViewModel @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recruitmentId: String = checkNotNull(savedStateHandle["recruitmentId"])

    private val _uiState = MutableStateFlow<RecruitmentDetailScreenUiState>(RecruitmentDetailScreenUiState.Loading)
    val uiState: StateFlow<RecruitmentDetailScreenUiState> = _uiState.asStateFlow()

    init {
        loadRecruitmentDetail()
    }

    private fun loadRecruitmentDetail() {
        viewModelScope.launch {
            _uiState.value = try {
                RecruitmentDetailScreenUiState.Success(recruitmentRepository.getRecruitmentDetail(recruitmentId))
            } catch (e: Exception) {
                RecruitmentDetailScreenUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    fun toggleBookmark() {
        val currentState = _uiState.value
        if (currentState is RecruitmentDetailScreenUiState.Success) {
            _uiState.value = currentState.copy(
                recruitment = currentState.recruitment.copy(isBookmarked = !currentState.recruitment.isBookmarked)
            )
        }
    }
}
