package com.umc.homefit.di

import com.umc.homefit.data.repository.recruitment.SavedNoticeRepositoryImpl
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SavedNoticeRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSavedNoticeRepository(impl: SavedNoticeRepositoryImpl): SavedNoticeRepository
}
