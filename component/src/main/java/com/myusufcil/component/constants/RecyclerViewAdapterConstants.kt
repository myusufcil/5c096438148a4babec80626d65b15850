package com.myusufcil.component.constants

import com.myusufcil.component.ui.stationdata.StationDataViewHolder

class RecyclerViewAdapterConstants {

    object TYPES {
        const val TYPE_NONE = 0
        const val TYPE_STATION_DATA = 1
    }

    val binderFactoryMap = mutableMapOf(
        TYPES.TYPE_STATION_DATA to StationDataViewHolder.BinderFactory(),
    )
    val holderFactoryMap = mutableMapOf(
        TYPES.TYPE_STATION_DATA to StationDataViewHolder.HolderFactory(),
    )
}