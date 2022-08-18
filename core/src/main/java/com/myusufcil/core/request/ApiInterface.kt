package com.myusufcil.core.request

import com.myusufcil.data.response.GetSpaceShipResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET(value = "v3/e7211664-cbb6-4357-9c9d-f12bf8bab2e2")
    suspend fun getSpaceStations(): List<GetSpaceShipResponse>
}