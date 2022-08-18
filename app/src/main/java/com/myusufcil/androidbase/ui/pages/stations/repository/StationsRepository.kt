package com.myusufcil.androidbase.ui.pages.stations.repository

import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.extension.*
import com.myusufcil.core.networking.DataFetchResult
import com.myusufcil.data.response.GetSpaceShipResponse
import com.myusufcil.core.networking.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class StationsRepository @Inject constructor(
    private val remoteData: StationsRemoteData,
    private val localData: StationsLocalData
) : StationsContract.Repository {

    override fun getStationFromApi(): Flow<DataFetchResult<List<GetSpaceShipResponse>>> =
        flow {
            val response = remoteData.getStationsFromApi()
            success(response)
        }.onStart {
            loading(true)
        }.catch { _error ->
            handleError(this, _error)
        }

    override fun getStationFromLocal(): Flow<DataFetchResult<List<StationsEntity>>> =
        flow {
            val response = localData.getStationsFromLocal()
            success(response)
        }.onStart {
            loading(true)
        }.catch { _error ->
            handleError(this, _error)
        }

    override suspend fun updateDataBase(id: Int, stock: Int, need: Int, isTaskDone: Boolean?) =
        localData.updateDataBase(id = id, stock = stock, need = need, isTaskDone = isTaskDone)

    override suspend fun insertAll(stations: List<StationsEntity>) =
        localData.insertAll(stations)

    override suspend fun insertDeleteFav(isFav: Boolean, idFav: Int) =
        localData.insertDeleteFav(isFav, idFav)

    override suspend fun getStatusOfTasks(): List<Boolean> =
        localData.getStatusOfTasks()

    override suspend fun <T> handleError(
        result: FlowCollector<DataFetchResult<T>>,
        error: Throwable
    ) {
        result.failed(error)
        Timber.e(error)
    }

}