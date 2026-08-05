package com.umc.homefit.data.repository.finance

import com.umc.homefit.data.api.finance.FinanceApiService
import com.umc.homefit.data.dto.finance.FinanceMatchResultDto
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.finance.FinanceRepository
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val financeApiService: FinanceApiService
) : FinanceRepository {

    override suspend fun getMatchedProducts(
        providerType: String?
    ): NetworkResult<FinanceMatchResultDto> {
        return safeApiCall {
            financeApiService.getMatchedProducts(providerType)
        }
    }
}
