package com.umc.homefit.data.datasource

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    val accessToken: Flow<String?>
    suspend fun saveAccessToken(token: String)
    suspend fun clearAccessToken()
}
