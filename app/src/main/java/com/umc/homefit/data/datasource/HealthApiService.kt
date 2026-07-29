package com.umc.homefit.data.datasource

import retrofit2.http.GET
import retrofit2.Response

interface HealthApiService {

    @GET("health")
    suspend fun getHealth(): Response<Unit>

}
