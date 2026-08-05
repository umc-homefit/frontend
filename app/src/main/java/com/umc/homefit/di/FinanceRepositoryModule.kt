package com.umc.homefit.di

import com.umc.homefit.data.repository.finance.FinanceRepositoryImpl
import com.umc.homefit.domain.repository.finance.FinanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FinanceRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFinanceRepository(
        impl: FinanceRepositoryImpl
    ): FinanceRepository
}
