package com.myusufcil.androidbase.ui.pages.favorites.repository

import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.extension.failed
import com.myusufcil.core.extension.loading
import com.myusufcil.core.extension.success
import com.myusufcil.core.networking.DataFetchResult
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val localData: FavoritesLocalData
) : FavoritesContract.Repository {
    override suspend fun insertDeleteFav(isFav: Boolean, idFav: Int) =
        localData.insertDeleteFav(isFav, idFav)

    override suspend fun getFavList(): Flow<DataFetchResult<List<StationsEntity>>> =
        flow {
            val response = localData.getFavList()
            success(response)
        }.onStart {
            loading(true)
        }.catch { _error ->
            handleError(this, _error)
        }


    override suspend fun <T> handleError(
        result: FlowCollector<DataFetchResult<T>>,
        error: Throwable
    ) {
        result.failed(error)
        Timber.e(error)
    }


}