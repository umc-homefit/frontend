package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.recruitment.NoticeDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.home.HomeRepository
import com.umc.homefit.domain.repository.mypage.MyPageRepository
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val savedNoticeRepository: SavedNoticeRepository,
    private val myPageRepository: MyPageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    init {
        loadUserName()
        loadFeaturedNotices()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            when (val result = myPageRepository.getProfile()) {
                is NetworkResult.Success -> {
                    _userName.value = result.data.nickname
                }

                is NetworkResult.Error -> Unit
            }
        }
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
                            updateSuccess(closingSoonNotices)
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

        val notice = currentState.notices.find { it.noticeId == noticeId } ?: return
        val nextSaved = !notice.isSaved
        applySavedState(noticeId, nextSaved)

        viewModelScope.launch {
            val isSuccess = if (nextSaved) {
                savedNoticeRepository.saveNotice(noticeId) is NetworkResult.Success
            } else {
                savedNoticeRepository.unsaveNotice(noticeId) is NetworkResult.Success
            }

            if (!isSuccess) {
                applySavedState(noticeId, notice.isSaved)
            }
        }
    }

    private fun applySavedState(noticeId: Long, isSaved: Boolean) {
        val currentState = _uiState.value
        if (currentState !is HomeScreenUiState.Success) return

        _uiState.value = currentState.copy(
            notices = currentState.notices.map { notice ->
                if (notice.noticeId == noticeId) notice.copy(isSaved = isSaved) else notice
            }
        )
    }

    private fun updateSuccess(notices: List<NoticeDto>) {
        _uiState.value = HomeScreenUiState.Success(notices = notices)
    }

    private companion object {
        const val STATUS_CLOSING_SOON = "CLOSING_SOON"
        const val STATUS_RECRUITING = "RECRUITING"
        const val SORT_DEADLINE = "DEADLINE"
        const val FIRST_PAGE = 0
        const val MAX_FEATURED_NOTICES = 4
    }
}
