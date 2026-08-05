package com.umc.homefit.data.api.finance

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.finance.FinanceMatchResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceApiService {

    @GET("loan-products/match")
    suspend fun getMatchedProducts(
        @Query("providerType") providerType: String? = null
    ): BaseResponse<FinanceMatchResultDto>
}
