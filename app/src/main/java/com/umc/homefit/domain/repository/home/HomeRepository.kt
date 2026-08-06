package com.umc.homefit.domain.repository.home

import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.model.home.FeaturedNotice

interface HomeRepository {
    suspend fun getNotices(
        status: String,
        sort: String,
        page: Int,
        size: Int
    ): NetworkResult<List<FeaturedNotice>>
}
