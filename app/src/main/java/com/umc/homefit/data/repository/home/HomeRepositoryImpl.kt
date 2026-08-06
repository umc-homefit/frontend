package com.umc.homefit.data.repository.home

import com.umc.homefit.data.api.recruitment.NoticeApiService
import com.umc.homefit.data.dto.recruitment.NoticeSummaryResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.model.home.FeaturedNotice
import com.umc.homefit.domain.model.home.FeaturedNoticeStatus
import com.umc.homefit.domain.repository.home.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val noticeApiService: NoticeApiService
) : HomeRepository {

    override suspend fun getNotices(
        status: String,
        sort: String,
        page: Int,
        size: Int
    ): NetworkResult<List<FeaturedNotice>> =
        when (
            val result = safeApiCall {
                noticeApiService.getNotices(
                    status = status,
                    sort = sort,
                    page = page,
                    size = size
                )
            }
        ) {
            is NetworkResult.Success -> NetworkResult.Success(
                result.data.notices.map(NoticeSummaryResponse::toDomain)
            )
            is NetworkResult.Error -> result
        }
}

private fun NoticeSummaryResponse.toDomain(): FeaturedNotice = FeaturedNotice(
    noticeId = noticeId,
    title = title,
    region = region,
    district = district,
    unitSummary = unitSummary,
    depositMin = depositMin,
    depositMax = depositMax,
    monthlyRentMin = monthlyRentMin,
    monthlyRentMax = monthlyRentMax,
    status = status.toFeaturedNoticeStatus(),
    statusDisplayText = statusDisplayText,
    isAdditionalRecruitment = isAdditionalRecruitment,
    applicationStartAt = applicationStartAt,
    applicationEndAt = applicationEndAt,
    dDayText = dDayText,
    views = views,
    interestedCount = interestedCount,
    isSaved = isSaved
)

private fun String.toFeaturedNoticeStatus(): FeaturedNoticeStatus = when (this) {
    "RECRUITING" -> FeaturedNoticeStatus.RECRUITING
    "SCHEDULED" -> FeaturedNoticeStatus.SCHEDULED
    "CLOSING_SOON" -> FeaturedNoticeStatus.CLOSING_SOON
    "CLOSED" -> FeaturedNoticeStatus.CLOSED
    else -> FeaturedNoticeStatus.UNKNOWN
}
