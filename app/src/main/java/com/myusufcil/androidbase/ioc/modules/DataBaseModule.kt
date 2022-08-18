package com.myusufcil.androidbase.ioc.modules

import android.content.Context
import androidx.room.Room
import com.myusufcil.androidbase.ioc.builder.AppDatabase
import com.myusufcil.core.database.dao.SpaceStationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): SpaceStationsDao {
        return appDatabase.spaceStationsDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            "space_stations.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}
