package com.umc.homefit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.homefit.data.dto.notification.AlertSettingsResponse
import com.umc.homefit.data.dto.notification.UpdateAlertSettingsRequest
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.notification.AlertSettingsRepository
import com.umc.homefit.domain.repository.recruitment.DistrictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val ALL_REGIONS = "전체"

@HiltViewModel
class InterestedRegionScreenViewModel @Inject constructor(
    private val alertSettingsRepository: AlertSettingsRepository,
    private val districtRepository: DistrictRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InterestedRegionScreenUiState>(
        InterestedRegionScreenUiState.Loading
    )
    val uiState: StateFlow<InterestedRegionScreenUiState> = _uiState.asStateFlow()

    // 화면에는 안 보이지만 PUT 풀바디 전송에 필요한 나머지 alert 설정값 스냅샷
    private var lastSettings: AlertSettingsResponse? = null

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = InterestedRegionScreenUiState.Loading
            val regions = districtRepository.getDistricts()

            when (val result = alertSettingsRepository.getAlertSettings()) {
                is NetworkResult.Success -> {
                    lastSettings = result.data
                    _uiState.value = InterestedRegionScreenUiState.Success(
                        regions = regions,
                        selectedRegion = result.data.interestedRegion ?: ALL_REGIONS
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.value = InterestedRegionScreenUiState.Error(result.message)
                }
            }
        }
    }

    fun onSelectRegion(region: String) {
        val current = _uiState.value as? InterestedRegionScreenUiState.Success ?: return
        val snapshot = lastSettings ?: return
        val previousRegion = current.selectedRegion

        _uiState.value = current.copy(selectedRegion = region) // 낙관적 업데이트

        viewModelScope.launch {
            val request = UpdateAlertSettingsRequest(
                pushEnabled = snapshot.pushEnabled,
                noticeAlertEnabled = snapshot.noticeAlertEnabled,
                scheduleAlertEnabled = snapshot.scheduleAlertEnabled,
                financeAlertEnabled = snapshot.financeAlertEnabled,
                interestedRegion = if (region == ALL_REGIONS) null else region
            )

            when (val result = alertSettingsRepository.updateAlertSettings(request)) {
                is NetworkResult.Success -> {
                    lastSettings = result.data
                }

                is NetworkResult.Error -> {
                    val rolledBack = _uiState.value
                    if (rolledBack is InterestedRegionScreenUiState.Success) {
                        _uiState.value = rolledBack.copy(selectedRegion = previousRegion)
                    }
                }
            }
        }
    }
}
