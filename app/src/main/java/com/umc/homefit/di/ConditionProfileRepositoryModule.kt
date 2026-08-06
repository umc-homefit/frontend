package com.umc.homefit.di

import com.umc.homefit.data.repository.finance.ConditionProfileRepositoryImpl
import com.umc.homefit.domain.repository.finance.ConditionProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConditionProfileRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindConditionProfileRepository(impl: ConditionProfileRepositoryImpl): ConditionProfileRepository
}
