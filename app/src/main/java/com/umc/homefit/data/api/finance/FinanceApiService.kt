package com.umc.homefit.data.api.finance

import com.umc.homefit.data.dto.common.BaseResponse
import com.umc.homefit.data.dto.finance.LoanProductsMatchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceApiService {
    @GET("loan-products/match")
    suspend fun getMatchedLoanProducts(
        @Query("providerType") providerType: String? = null,
        @Query("productCategory") productCategory: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("sort") sort: String?
    ): BaseResponse<LoanProductsMatchResponse>
}
