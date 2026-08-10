package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.mypage.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {

    private val _recommendedAnnouncements =
        MutableStateFlow(sampleAnnouncements)

    val recommendedAnnouncements =
        _recommendedAnnouncements.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    init {
        loadUserName()
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
