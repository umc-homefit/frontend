package com.umc.homefit.data.datasource

import com.umc.homefit.data.dto.HealthResponse
import retrofit2.http.GET
import retrofit2.Response

interface HealthApiService {

    @GET("health")
    suspend fun getHealth(): Response<Unit>

}
