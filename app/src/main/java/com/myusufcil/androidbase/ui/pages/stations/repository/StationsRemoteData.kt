package com.myusufcil.androidbase.ui.pages.stations.repository

import com.myusufcil.core.request.ApiInterface
import com.myusufcil.data.response.GetSpaceShipResponse
import javax.inject.Inject

class StationsRemoteData @Inject constructor(
    private val apiService: ApiInterface
) : StationsContract.Remote {

    override suspend fun getStationsFromApi(): List<GetSpaceShipResponse> =
        apiService.getSpaceStations()
}