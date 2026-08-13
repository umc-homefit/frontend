package com.umc.homefit.di

import com.umc.homefit.data.repository.mypage.MyPageRepositoryImpl
import com.umc.homefit.domain.repository.mypage.MyPageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyPageRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMyPageRepository(impl: MyPageRepositoryImpl) : MyPageRepository
}
