package com.myusufcil.androidbase.ui.pages.stations.repository

import com.myusufcil.core.database.dao.SpaceStationsDao
import com.myusufcil.core.database.model.StationsEntity
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StationsLocalData @Inject constructor(
    private val stationsDao: SpaceStationsDao
) : StationsContract.Local {

    override suspend fun updateDataBase(id: Int, stock: Int, need: Int,isTaskDone: Boolean?) =
        stationsDao.updateDataBase(stock = stock, need = need,id= id, isTaskDone = isTaskDone)

    override suspend fun getStationsFromLocal(): List<StationsEntity> =
        stationsDao.getAll()

    override suspend fun insertAll(stations: List<StationsEntity>) =
        stationsDao.insertAll(stations)

    override suspend fun insertDeleteFav(isFav: Boolean, idFav: Int) =
        stationsDao.insertFav(isFav, idFav)

    override suspend fun deleteAll() =
        stationsDao.deleteAll()

    override suspend fun getStatusOfTasks(): List<Boolean> =
        stationsDao.getStatusOfTasks()


}