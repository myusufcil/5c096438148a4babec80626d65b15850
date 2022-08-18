package com.myusufcil.component.ui.stationdata

import android.os.Parcelable
import com.myusufcil.component.constants.RecyclerViewAdapterConstants.TYPES.TYPE_STATION_DATA
import com.myusufcil.core.enum.PageStatus
import com.myusufcil.core.helpers.LocalPrefManager
import com.myusufcil.core.recyclerview.DisplayItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class StationDataDTO(
    var id: Int,
    var name: String?,
    var coordinateX: Double?,
    var coordinateY: Double?,
    var capacity: Int?,
    var stock: Int?,
    var need: Int?,
    var isFav: Boolean = false,
    var isTaskDone: Boolean = false,
    var pageInfo: String? = PageStatus.STATIONS_PAGE.value,
    var eus :Double?
) : Parcelable, DisplayItem(TYPE_STATION_DATA)