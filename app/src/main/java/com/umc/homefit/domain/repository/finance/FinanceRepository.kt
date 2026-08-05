package com.umc.homefit.domain.repository.finance

import com.umc.homefit.data.dto.finance.FinanceMatchResultDto
import com.umc.homefit.data.remote.NetworkResult

interface FinanceRepository {

    suspend fun getMatchedProducts(
        providerType: String? = null
    ): NetworkResult<FinanceMatchResultDto>
}
