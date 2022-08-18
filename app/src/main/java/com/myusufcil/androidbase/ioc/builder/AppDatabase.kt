package com.myusufcil.androidbase.ioc.builder

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myusufcil.core.database.dao.SpaceStationsDao
import com.myusufcil.core.database.model.StationsEntity


@Database(entities = [StationsEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spaceStationsDao(): SpaceStationsDao
}
