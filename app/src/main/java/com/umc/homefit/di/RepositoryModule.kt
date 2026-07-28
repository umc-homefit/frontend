package com.umc.homefit.di

import com.umc.homefit.data.datasource.RemoteDataSource
import com.umc.homefit.data.datasource.RemoteDataSourceImpl
import com.umc.homefit.data.repository.DistrictRepository
import com.umc.homefit.data.repository.DistrictRepositoryImpl
import com.umc.homefit.data.repository.HomeRepository
import com.umc.homefit.data.repository.HomeRepositoryImpl
import com.umc.homefit.data.repository.RecruitmentRepository
import com.umc.homefit.data.repository.RecruitmentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindRecruitmentRepository(recruitmentRepositoryImpl: RecruitmentRepositoryImpl): RecruitmentRepository

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDistrictRepository(districtRepositoryImpl: DistrictRepositoryImpl): DistrictRepository
}
