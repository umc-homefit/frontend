package com.umc.homefit.data.local

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
