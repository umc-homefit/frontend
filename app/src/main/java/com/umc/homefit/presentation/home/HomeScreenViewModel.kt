package com.umc.homefit.presentation.home


import androidx.lifecycle.ViewModel
import com.umc.homefit.data.dto.recruitment.RecruitmentDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {

    private val _recommendedAnnouncements =
        MutableStateFlow(sampleAnnouncements)

    val recommendedAnnouncements =
        _recommendedAnnouncements.asStateFlow()

    fun toggleBookmark(recruitmentId: String) {
        _recommendedAnnouncements.update { recruitments ->
            recruitments.map { recruitment ->
                if (recruitment.id == recruitmentId) {
                    recruitment.copy(
                        isBookmarked = !recruitment.isBookmarked
                    )
                } else {
                    recruitment
                }
            }
        }
    }
}
