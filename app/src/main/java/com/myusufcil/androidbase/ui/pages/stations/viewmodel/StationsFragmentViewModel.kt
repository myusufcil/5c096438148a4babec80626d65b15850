package com.myusufcil.androidbase.ui.pages.stations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myusufcil.androidbase.ui.pages.stations.repository.StationsRepository
import com.myusufcil.core.base.BaseViewModel
import com.myusufcil.core.networking.DataFetchResult
import com.myusufcil.core.recyclerview.DisplayItem
import com.myusufcil.data.response.GetSpaceShipResponse
import com.myusufcil.component.ui.stationdata.StationDataDTO
import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.enum.PageStatus
import com.myusufcil.core.enum.SharedPref
import com.myusufcil.core.helpers.LocalPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsFragmentViewModel @Inject constructor(
    var repository: StationsRepository
) : BaseViewModel() {

    val itemPublishSubject = PublishSubject.create<List<DisplayItem>>()

    private var taskStatusMutableLiveData = MutableLiveData<List<Boolean>>()
    val taskStatusLiveData: LiveData<List<Boolean>>
        get() {
            return taskStatusMutableLiveData
        }

    @Inject
    lateinit var localPrefManager: LocalPrefManager

    private var _updateRecyclerItemLD = MutableLiveData<Pair<Int, StationDataDTO>>()
    val updateRecyclerItemLD: LiveData<Pair<Int, StationDataDTO>>
        get() {
            return _updateRecyclerItemLD
        }

    fun getStationFromApi() {
        viewModelScope.launch {
            repository.getStationFromApi().collect { _response ->
                when (_response) {
                    is DataFetchResult.Success -> {
                        repository.insertAll(wrapDataList(_response.data))
                        getStationsFromLocal()
                    }
                    is DataFetchResult.Progress -> {}
                    is DataFetchResult.Failure -> {}
                }
            }
        }
    }

    fun insertDeleteFav(isFav: Boolean, id: Int, index: Int, list: List<DisplayItem>) =
        viewModelScope.launch {
            repository.insertDeleteFav(isFav, id)
            _updateRecyclerItemLD.value = Pair(index, (list[index] as StationDataDTO))
        }

    fun getStationsFromLocal() {
        viewModelScope.launch {
            repository.getStationFromLocal().collect { _response ->
                when (_response) {
                    is DataFetchResult.Success -> {
                        consumeData(_response.data)
                    }
                    is DataFetchResult.Progress -> {}
                    is DataFetchResult.Failure -> {}
                }
            }
        }
    }

    private fun wrapDataList(spaceShipResponse: List<GetSpaceShipResponse>): List<StationsEntity> =
        spaceShipResponse.mapIndexed { index, _station ->
            StationsEntity(
                id = index + 1,
                name = _station.name,
                coordinateX = _station.coordinateX,
                coordinateY = _station.coordinateY,
                capacity = _station.capacity,
                stock = _station.stock,
                need = _station.need
            )
        }

    private fun consumeData(spaceStations: List<StationsEntity>) =
        spaceStations.map { _station ->
            StationDataDTO(
                id = _station.id,
                name = _station.name,
                coordinateX = _station.coordinateX,
                coordinateY = _station.coordinateY,
                capacity = _station.capacity,
                stock = _station.stock,
                need = _station.need,
                isFav = _station.isFav,
                isTaskDone = _station.isTaskDone,
                pageInfo = PageStatus.STATIONS_PAGE.value,
                eus = distanceBetweenTwoPoints(
                    localPrefManager.pullString(SharedPref.PREF_X1.value, "0.0")
                        .toDouble(),
                    localPrefManager.pullString(SharedPref.PREF_Y1.value, "0.0")
                        .toDouble(),
                    _station.coordinateX ?: 0.0,
                    _station.coordinateY ?: 0.0
                )
            )
        }.takeIf { it.isNotEmpty() }?.let {
            itemPublishSubject.onNext(it)
        } ?: let {
            itemPublishSubject.onNext(emptyList())
        }

    fun travelUpdateUI(
        id: Int,
        item: StationDataDTO,
    ) =
        viewModelScope.launch {
            if (item.isTaskDone) {
                repository.updateDataBase(
                    id = id,
                    need = item.need ?: 0,
                    stock = item.stock ?: 0,
                    isTaskDone = item.isTaskDone
                )
                getStatusOfTask()
            }
            getStationsFromLocal()
        }

    fun getStatusOfTask() = viewModelScope.launch {
        taskStatusMutableLiveData.value = repository.getStatusOfTasks()
    }
}