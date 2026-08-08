package com.umc.homefit.data.repository.finance

import com.umc.homefit.data.api.finance.FinanceApiService
import com.umc.homefit.data.dto.finance.LoanProductDetailResponse
import com.umc.homefit.data.dto.finance.LoanProductsMatchResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.finance.FinanceRepository
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val financeApiService: FinanceApiService
) : FinanceRepository {
    override suspend fun getMatchedLoanProducts(
        providerType: String?,
        productCategory: String?,
        keyword: String?,
        sort: String?
    ): NetworkResult<LoanProductsMatchResponse> =
        safeApiCall {
            financeApiService.getMatchedLoanProducts(
                providerType = providerType,
                productCategory = productCategory,
                keyword = keyword,
                sort = sort
            )
        }

    override suspend fun getLoanProductDetail(
        productId: Long
    ): NetworkResult<LoanProductDetailResponse> =
        safeApiCall {
            financeApiService.getLoanProductDetail(productId)
        }
}
