package com.umc.homefit.data.api.analysis

import com.umc.homefit.data.dto.analysis.EligibilityAnalysisResultDto
import com.umc.homefit.data.dto.analysis.MyEligibilityAnalysesResultDto
import com.umc.homefit.data.dto.common.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnalysisApiService {

    @GET("users/me/eligibility-analyses")
    suspend fun getMyEligibilityAnalyses(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<MyEligibilityAnalysesResultDto>

    @GET("eligibility-analyses/{analysisId}")
    suspend fun getEligibilityAnalysis(
        @Path("analysisId") analysisId: Long
    ): BaseResponse<EligibilityAnalysisResultDto>

}
