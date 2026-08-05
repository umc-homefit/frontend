package com.umc.homefit.data.repository.finance

import com.umc.homefit.data.api.finance.FinanceApiService
import com.umc.homefit.data.dto.finance.LoanProductResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.model.finance.LoanProduct
import com.umc.homefit.domain.model.finance.LoanProductsMatch
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
    ): NetworkResult<LoanProductsMatch> =
        when (
            val result = safeApiCall {
                financeApiService.getMatchedLoanProducts(
                    providerType = providerType,
                    productCategory = productCategory,
                    keyword = keyword,
                    sort = sort
                )
            }
        ) {
            is NetworkResult.Success -> NetworkResult.Success(
                LoanProductsMatch(
                    matchedCount = result.data.matchedCount,
                    minRate = result.data.minRate,
                    maxLimitAmount = result.data.maxLimitAmount,
                    products = result.data.products.map(LoanProductResponse::toDomain)
                )
            )
            is NetworkResult.Error -> result
        }
}

private fun LoanProductResponse.toDomain(): LoanProduct = LoanProduct(
    productId = productId,
    productName = productName,
    providerType = providerType,
    productCategory = productCategory,
    providerName = providerName,
    rateRange = rateRange,
    maxIncome = maxIncome,
    firstTimeBuyerOnly = firstTimeBuyerOnly,
    incomeTaxDeductible = incomeTaxDeductible,
    maxLimitAmount = maxLimitAmount,
    minAge = minAge,
    maxAge = maxAge,
    requireNoHouse = requireNoHouse,
    minMonthlyDeposit = minMonthlyDeposit,
    maxMonthlyDeposit = maxMonthlyDeposit,
    isEligible = isEligible,
    ageCheckSkipped = ageCheckSkipped,
    householdHeadCheckSkipped = householdHeadCheckSkipped,
    marriedCheckSkipped = marriedCheckSkipped,
    newbornCheckSkipped = newbornCheckSkipped,
    firstTimeBuyerCheckSkipped = firstTimeBuyerCheckSkipped,
    ineligibleReasons = ineligibleReasons
)
