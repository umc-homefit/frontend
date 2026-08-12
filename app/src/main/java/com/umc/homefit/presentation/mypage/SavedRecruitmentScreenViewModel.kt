package com.umc.homefit.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.common.NoticeStatus
import com.umc.homefit.data.dto.recruitment.SavedNoticeResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import com.umc.homefit.presentation.component.NoticeCardUiModel
import com.umc.homefit.presentation.component.buildAreaDepositLine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecruitmentScreenViewModel @Inject constructor(
    private val savedNoticeRepository: SavedNoticeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<SavedRecruitmentScreenUiState>(SavedRecruitmentScreenUiState.Loading)
    val uiState: StateFlow<SavedRecruitmentScreenUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var isLoading = false

    init {
        loadSavedNotices(page = 0, isLoadMore = false)
    }

    fun onSortOptionSelected(option: SortOption) {
        val current = _uiState.value
        if (current is SavedRecruitmentScreenUiState.Success) {
            _uiState.value = current.copy(sortOption = option)
        }
        loadSavedNotices(page = 0, isLoadMore = false)
    }

    fun onRemoveClick(itemId: String) {
        viewModelScope.launch {
            val result = savedNoticeRepository.unsaveNotice(itemId.toLong())
            if (result is NetworkResult.Success) {
                val current = _uiState.value
                if (current is SavedRecruitmentScreenUiState.Success) {
                    _uiState.value = current.copy(items = current.items.filterNot { it.id == itemId })
                }
            }
        }
    }

    fun loadNextPage() {
        val current = _uiState.value
        if (current !is SavedRecruitmentScreenUiState.Success) return
        if (isLoading || !current.hasNext) return
        loadSavedNotices(page = currentPage + 1, isLoadMore = true)
    }

    private fun loadSavedNotices(page: Int, isLoadMore: Boolean) {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val current = _uiState.value
                if (isLoadMore && current is SavedRecruitmentScreenUiState.Success) {
                    _uiState.value = current.copy(isLoadingMore = true)
                } else if (!isLoadMore) {
                    _uiState.value = SavedRecruitmentScreenUiState.Loading
                }

                // TODO: 현재 LATEST(최신순)만 지원해서 일단 고정, 기타 옵션 추가 지원되면 sortOption 반영
                when (val result = savedNoticeRepository.getSavedNotices(sort = "LATEST", page = page, size = PAGE_SIZE)) {
                    is NetworkResult.Success -> {
                        currentPage = page
                        val newItems = result.data.savedNotices.map { it.toNoticeCardUiModel() }
                        val previousItems = if (isLoadMore && current is SavedRecruitmentScreenUiState.Success) current.items else emptyList()
                        val sortOption = if (current is SavedRecruitmentScreenUiState.Success) current.sortOption else SortOption.LATEST

                        _uiState.value = SavedRecruitmentScreenUiState.Success(
                            items = previousItems + newItems,
                            sortOption = sortOption,
                            isLoadingMore = false,
                            hasNext = result.data.pageInfo.hasNext
                        )
                    }
                    is NetworkResult.Error -> {
                        if (isLoadMore && current is SavedRecruitmentScreenUiState.Success) {
                            _uiState.value = current.copy(isLoadingMore = false)
                        } else {
                            _uiState.value = SavedRecruitmentScreenUiState.Error(result.message)
                        }
                    }
                }
            } finally {
                isLoading = false
            }
        }
    }
    private fun SavedNoticeResponse.toNoticeCardUiModel(): NoticeCardUiModel {
        return NoticeCardUiModel(
            id = noticeId.toString(),
            title = title,
            infoLine1 = announcementNo?.let { "공고번호 | $it" },
            infoLine2 = buildAreaDepositLine(unitSummary, depositMin),
            infoLine3 = "청약접수 | ${applicationStartAt?.toDateText() ?: "공고문 참고"} ~ ${applicationEndAt?.toDateText() ?: "공고문 참고"}",
            status = NoticeStatus.fromApiValue(status),
            statusLabel = statusDisplayText,
            isSaved = true
        )
    }

    private fun String.toDateText(): String = take(10).replace("-", ".")

    private companion object {
        const val PAGE_SIZE = 10
    }
}
