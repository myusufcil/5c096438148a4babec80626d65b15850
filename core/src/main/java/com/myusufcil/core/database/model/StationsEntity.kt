package com.myusufcil.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myusufcil.core.database.model.StationsEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class StationsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "station_name") var name: String?,
    @ColumnInfo(name = "coordinateX") var coordinateX: Double?,
    @ColumnInfo(name = "coordinateY") var coordinateY: Double?,
    @ColumnInfo(name = "capacity") var capacity: Int?,
    @ColumnInfo(name = "stock") var stock: Int?,
    @ColumnInfo(name = "need") var need: Int?,
    @ColumnInfo(name = "isFav") var isFav: Boolean = false,
    @ColumnInfo(name = "isTaskDone") var isTaskDone: Boolean = false,
    @ColumnInfo(name = "eus") var eus: Int = 0
) {
    companion object {
        const val TABLE_NAME = "stations"
    }
    init {
        if (name == "Dünya") {
            isTaskDone = true
        }
    }
}
