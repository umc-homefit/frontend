package com.umc.homefit.data.local

import com.umc.homefit.data.datasource.local.UserPreferencesDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : TokenProvider {
    override fun getAccessToken(): String? = runBlocking {
        userPreferencesDataSource.accessToken.first()
    }
}
