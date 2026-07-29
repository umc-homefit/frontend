package com.umc.homefit.data.repository.auth

import com.umc.homefit.data.datasource.AuthRemoteDataSource
import com.umc.homefit.data.datasource.local.UserPreferencesDataSource
import com.umc.homefit.data.dto.auth.LoginResponse
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResult<LoginResponse> {
        val result = authRemoteDataSource.login(email, password)
        if (result is NetworkResult.Success) {
            userPreferencesDataSource.updateAccessToken(result.data.accessToken)
        }
        return result
    }
}
