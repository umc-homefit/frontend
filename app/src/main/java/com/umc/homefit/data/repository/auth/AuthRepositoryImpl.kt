package com.umc.homefit.data.repository.auth

import com.umc.homefit.data.api.auth.AuthApiService
import com.umc.homefit.data.local.UserPreferencesDataSource
import com.umc.homefit.data.dto.auth.LoginRequest
import com.umc.homefit.data.dto.auth.LoginResponse
import com.umc.homefit.data.dto.auth.SignupRequest
import com.umc.homefit.data.dto.auth.SocialAuthRequest
import com.umc.homefit.data.remote.NetworkResult
import com.umc.homefit.data.remote.safeApiCall
import com.umc.homefit.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResult<LoginResponse> {
        val result = safeApiCall { authApiService.login(LoginRequest(email = email, password = password)) }
        if (result is NetworkResult.Success) {
            userPreferencesDataSource.updateAccessToken(result.data.accessToken)
        }
        return result
    }

    override suspend fun signup(email: String, password: String): NetworkResult<LoginResponse> {
        val result = safeApiCall { authApiService.signup(SignupRequest(email = email, password = password)) }
        if (result is NetworkResult.Success) {
            userPreferencesDataSource.updateAccessToken(result.data.accessToken)
        }
        return result
    }

    override suspend fun socialLogin(provider: String, oauthToken: String): NetworkResult<LoginResponse> {
        val result = safeApiCall { authApiService.socialLogin(
            SocialAuthRequest(
                provider = provider,
                oauthToken = oauthToken
            )
        ) }
        if (result is NetworkResult.Success) {
            userPreferencesDataSource.updateAccessToken(result.data.accessToken)
        }
        return result
    }

    override suspend fun logout() {
        safeApiCall { authApiService.logout() }
        userPreferencesDataSource.clearAccessToken()
    }
}
