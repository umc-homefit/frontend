package com.umc.homefit.di

import com.umc.homefit.domain.repository.recruitment.DistrictRepository
import com.umc.homefit.data.repository.recruitment.DistrictRepositoryImpl
import com.umc.homefit.domain.repository.recruitment.RecruitmentRepository
import com.umc.homefit.data.repository.recruitment.RecruitmentRepositoryImpl
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
}
