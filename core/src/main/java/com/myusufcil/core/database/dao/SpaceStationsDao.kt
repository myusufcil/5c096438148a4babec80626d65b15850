package com.myusufcil.core.database.dao

import androidx.room.*
import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.database.model.StationsEntity.Companion.TABLE_NAME

@Dao
interface SpaceStationsDao {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAll(): List<StationsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(stations: List<StationsEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_NAME where isFav=1")
    suspend fun getFavorites(): List<StationsEntity>

    @Query("UPDATE $TABLE_NAME SET isFav=:isFav WHERE id = :id")
    suspend fun insertFav(isFav: Boolean?, id: Int)

    @Query("UPDATE $TABLE_NAME SET stock=:stock, need =:need,isTaskDone=:isTaskDone WHERE id = :id")
    suspend fun updateDataBase(id: Int, stock: Int, need: Int, isTaskDone: Boolean?)

    @Query("SELECT isTaskDone FROM $TABLE_NAME ")
    suspend fun getStatusOfTasks(): List<Boolean>
}
