package com.umc.homefit.domain.repository.finance

import com.umc.homefit.data.dto.finance.LoanProductsMatchResponse
import com.umc.homefit.data.remote.NetworkResult

interface FinanceRepository {
    suspend fun getMatchedLoanProducts(
        providerType: String? = null,
        productCategory: String? = null,
        keyword: String? = null,
        sort: String? = "RECOMMENDED"
    ): NetworkResult<LoanProductsMatchResponse>
}
