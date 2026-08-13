package com.umc.homefit.di

import com.umc.homefit.data.repository.notification.AlertSettingsRepositoryImpl
import com.umc.homefit.domain.repository.notification.AlertSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlertSettingsRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAlertSettingsRepository(
        impl: AlertSettingsRepositoryImpl
    ): AlertSettingsRepository
}
