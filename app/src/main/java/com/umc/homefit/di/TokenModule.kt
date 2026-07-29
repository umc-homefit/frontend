package com.umc.homefit.di

import com.umc.homefit.data.local.TokenProvider
import com.umc.homefit.data.local.TokenProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {
    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: TokenProviderImpl): TokenProvider
}
