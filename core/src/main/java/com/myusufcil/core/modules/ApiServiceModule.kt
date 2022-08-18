package com.myusufcil.core.modules

import com.myusufcil.core.request.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)
}