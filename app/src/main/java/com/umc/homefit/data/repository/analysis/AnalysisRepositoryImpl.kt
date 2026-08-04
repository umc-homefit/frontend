package com.umc.homefit.data.repository.analysis

import com.umc.homefit.data.api.analysis.AnalysisApiService
import com.umc.homefit.data.dto.analysis.MyEligibilityAnalysesResultDto
import com.umc.homefit.data.mock.AnalysisRecordMockData
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.analysis.AnalysisRepository
import javax.inject.Inject

class AnalysisRepositoryImpl @Inject constructor(
    private val analysisApiService: AnalysisApiService
) : AnalysisRepository {

    override suspend fun getMyEligibilityAnalyses(page: Int, size: Int): NetworkResult<MyEligibilityAnalysesResultDto> {
        // TODO: DB 데이터 채워지면 아래 mock 줄 지우고 주석 해제
        return NetworkResult.Success(AnalysisRecordMockData.getRecords())
        // return safeApiCall { analysisApiService.getMyEligibilityAnalyses(page = page, size = size) }
    }
}
