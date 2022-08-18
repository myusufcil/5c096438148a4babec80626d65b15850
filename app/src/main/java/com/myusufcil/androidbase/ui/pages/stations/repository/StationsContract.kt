package com.myusufcil.androidbase.ui.pages.stations.repository

import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.networking.DataFetchResult
import com.myusufcil.data.response.GetSpaceShipResponse
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface StationsContract {

    interface Remote {
        suspend fun getStationsFromApi(): List<GetSpaceShipResponse>
    }

    interface Local {
        suspend fun updateDataBase(id: Int, stock: Int, need: Int, isTaskDone: Boolean?)
        suspend fun getStationsFromLocal(): List<StationsEntity>
        suspend fun insertAll(stations: List<StationsEntity>)
        suspend fun insertDeleteFav(isFav: Boolean, idFav: Int)
        suspend fun deleteAll()
        suspend fun getStatusOfTasks(): List<Boolean>
    }

    interface Repository {
        fun getStationFromLocal(): Flow<DataFetchResult<List<StationsEntity>>>
        fun getStationFromApi(): Flow<DataFetchResult<List<GetSpaceShipResponse>>>
        suspend fun getStatusOfTasks(): List<Boolean>
        suspend fun updateDataBase(id: Int, stock: Int, need: Int, isTaskDone: Boolean?)
        suspend fun insertAll(stations: List<StationsEntity>)
        suspend fun insertDeleteFav(isFav: Boolean, idFav: Int)
        suspend fun <T> handleError(result: FlowCollector<DataFetchResult<T>>, error: Throwable)
    }
}