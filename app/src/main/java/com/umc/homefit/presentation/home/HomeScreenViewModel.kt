package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.model.home.FeaturedNotice
import com.umc.homefit.domain.repository.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
                    val closingSoonNotices = closingSoonResult.data.take(MAX_FEATURED_NOTICES)
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
                                notices = (closingSoonNotices + recruitingResult.data)
                                    .distinctBy(FeaturedNotice::noticeId)
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

    private fun updateSuccess(notices: List<FeaturedNotice>) {
        _uiState.value = HomeScreenUiState.Success(
            notices = notices.map(FeaturedNotice::toUiModel)
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

private fun FeaturedNotice.toUiModel(): HomeNoticeUiModel = HomeNoticeUiModel(
    noticeId = noticeId,
    title = title,
    location = listOfNotNull(region, district).joinToString(" "),
    unitSummary = unitSummary ?: "-",
    deposit = formatAmountRange(depositMin, depositMax),
    applicationPeriod = "${applicationStartAt.toDateText()} ~ ${applicationEndAt.toDateText()}",
    status = status,
    statusDisplayText = statusDisplayText.ifBlank { "기타" },
    dDayText = dDayText,
    isSaved = isSaved
)

private fun formatAmountRange(min: Long?, max: Long?): String = when {
    min == null && max == null -> "-"
    min == max -> min.toWonText()
    min == null -> "최대 ${max.toWonText()}"
    max == null -> "최소 ${min.toWonText()}"
    else -> "${min.toWonText()} ~ ${max.toWonText()}"
}

private fun Long?.toWonText(): String =
    this?.let { "${NumberFormat.getNumberInstance(Locale.KOREA).format(it / 10_000)}만원" } ?: "-"

private fun String?.toDateText(): String =
    this?.substringBefore('T')?.replace('-', '.') ?: "-"
