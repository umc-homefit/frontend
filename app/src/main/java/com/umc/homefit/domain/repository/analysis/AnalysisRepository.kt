package com.umc.homefit.domain.repository.analysis

import com.umc.homefit.data.dto.analysis.MyEligibilityAnalysesResultDto
import com.umc.homefit.data.remote.NetworkResult

interface AnalysisRepository {
    suspend fun getMyEligibilityAnalyses(page: Int, size: Int): NetworkResult<MyEligibilityAnalysesResultDto>
}
