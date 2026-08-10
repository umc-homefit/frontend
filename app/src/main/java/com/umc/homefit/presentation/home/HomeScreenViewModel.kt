package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        loadFeaturedNotices()
    }

    fun loadFeaturedNotices() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUiState.Loading

            when (
                val closingSoonResult = homeRepository.getNotices(
                    status = STATUS_CLOSING_SOON,
                    sort = SORT_DEADLINE,
                    page = FIRST_PAGE,
                    size = MAX_FEATURED_NOTICES
                )
            ) {
                is NetworkResult.Error -> {
                    _uiState.value = HomeScreenUiState.Error(closingSoonResult.message)
                }
                is NetworkResult.Success -> {
                    val closingSoonNotices = closingSoonResult.data.notices
                        .take(MAX_FEATURED_NOTICES)
                    val remainingCount = MAX_FEATURED_NOTICES - closingSoonNotices.size

                    if (remainingCount == 0) {
                        updateSuccess(closingSoonNotices)
                        return@launch
                    }

                    when (
                        val recruitingResult = homeRepository.getNotices(
                            status = STATUS_RECRUITING,
                            sort = SORT_DEADLINE,
                            page = FIRST_PAGE,
                            size = remainingCount
                        )
                    ) {
                        is NetworkResult.Error -> {
                            _uiState.value = HomeScreenUiState.Error(recruitingResult.message)
                        }
                        is NetworkResult.Success -> {
                            updateSuccess(
                                notices = (closingSoonNotices + recruitingResult.data.notices)
                                    .distinctBy(NoticeDto::noticeId)
                                    .take(MAX_FEATURED_NOTICES)
                            )
                        }
                    }
                }
            }
        }
    }

    fun toggleBookmark(noticeId: Long) {
        val currentState = _uiState.value
        if (currentState !is HomeScreenUiState.Success) return

        _uiState.value = currentState.copy(
            notices = currentState.notices.map { notice ->
                if (notice.noticeId == noticeId) {
                    notice.copy(isSaved = !notice.isSaved)
                } else {
                    notice
                }
            }
        )
    }

    private fun updateSuccess(notices: List<NoticeDto>) {
        _uiState.value = HomeScreenUiState.Success(
            notices = notices
        )
    }

    private companion object {
        const val STATUS_CLOSING_SOON = "CLOSING_SOON"
        const val STATUS_RECRUITING = "RECRUITING"
        const val SORT_DEADLINE = "DEADLINE"
        const val FIRST_PAGE = 0
        const val MAX_FEATURED_NOTICES = 4
    }
}
