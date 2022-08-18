package com.myusufcil.androidbase.ui.pages.main.repository

import com.myusufcil.androidbase.ui.pages.stations.repository.StationsLocalData
import com.myusufcil.core.extension.failed
import com.myusufcil.core.networking.DataFetchResult
import kotlinx.coroutines.flow.FlowCollector
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val localData: StationsLocalData
) : MainContract.Repository {

    override suspend fun deleteAll() =
        localData.deleteAll()

}