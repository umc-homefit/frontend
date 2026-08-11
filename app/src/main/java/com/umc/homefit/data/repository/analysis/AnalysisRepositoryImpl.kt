package com.umc.homefit.data.repository.analysis

import com.umc.homefit.data.api.analysis.AnalysisApiService
import com.umc.homefit.data.dto.analysis.EligibilityAnalysisResultDto
import com.umc.homefit.data.dto.analysis.MyEligibilityAnalysesResultDto
import com.umc.homefit.data.dto.analysis.RequestEligibilityAnalysisResultDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import javax.inject.Inject

class AnalysisRepositoryImpl @Inject constructor(
    private val analysisApiService: AnalysisApiService
) : AnalysisRepository {

    override suspend fun getMyEligibilityAnalyses(page: Int, size: Int): NetworkResult<MyEligibilityAnalysesResultDto> {
        return safeApiCall { analysisApiService.getMyEligibilityAnalyses(page = page, size = size) }
    }

    override suspend fun getEligibilityAnalysis(analysisId: Long): NetworkResult<EligibilityAnalysisResultDto> {
        return safeApiCall { analysisApiService.getEligibilityAnalysis(analysisId) }
    }

    override suspend fun requestEligibilityAnalysis(noticeId: Long, unitId: Long): NetworkResult<RequestEligibilityAnalysisResultDto> {
        return safeApiCall { analysisApiService.requestEligibilityAnalysis(noticeId = noticeId, unitId = unitId) }
    }
}
