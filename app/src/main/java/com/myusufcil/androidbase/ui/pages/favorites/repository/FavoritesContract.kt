package com.myusufcil.androidbase.ui.pages.favorites.repository

import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.networking.DataFetchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface FavoritesContract {

    interface Local {
        suspend fun insertDeleteFav(isFav:Boolean,idFav: Int)
        suspend fun getFavList(): List<StationsEntity>
    }

    interface Repository {
        suspend fun insertDeleteFav(isFav:Boolean,idFav: Int)
        suspend fun getFavList(): Flow<DataFetchResult<List<StationsEntity>>>
        suspend fun <T> handleError(result: FlowCollector<DataFetchResult<T>>, error: Throwable)
    }
}