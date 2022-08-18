package com.myusufcil.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetSpaceShipResponse(
    var name :String?,
    var coordinateX:Double?,
    var coordinateY:Double?,
    var capacity:Int?,
    var stock:Int?,
    var need:Int?
) : Parcelable