package com.myusufcil.core.modules

import android.content.Context
import android.content.SharedPreferences
import com.myusufcil.core.constants.AppConstants.PREF_NAME
import com.myusufcil.core.helpers.LocalPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemServiceModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLocalPrefManager(sharedPreferences: SharedPreferences): LocalPrefManager =
        LocalPrefManager(sharedPreferences)
}