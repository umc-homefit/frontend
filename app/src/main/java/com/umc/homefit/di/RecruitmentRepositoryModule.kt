package com.umc.homefit.di

import com.umc.homefit.domain.repository.recruitment.DistrictRepository
import com.umc.homefit.data.repository.recruitment.DistrictRepositoryImpl
import com.umc.homefit.domain.repository.recruitment.NoticeDetailRepository
import com.umc.homefit.data.repository.recruitment.NoticeDetailRepositoryImpl
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import com.umc.homefit.data.repository.recruitment.RecruitmentRepositoryImpl
import com.umc.homefit.data.repository.recruitment.SavedNoticeRepositoryImpl
import com.umc.homefit.domain.repository.recruitment.SavedNoticeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecruitmentRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecruitmentRepository(impl: RecruitmentRepositoryImpl): RecruitmentRepository

    @Binds
    @Singleton
    abstract fun bindDistrictRepository(districtRepositoryImpl: DistrictRepositoryImpl): DistrictRepository

    @Binds
    @Singleton
    abstract fun bindSavedNoticeRepository(impl: SavedNoticeRepositoryImpl): SavedNoticeRepository

    @Binds
    @Singleton
    abstract fun bindNoticeDetailRepository(impl: NoticeDetailRepositoryImpl): NoticeDetailRepository
}
