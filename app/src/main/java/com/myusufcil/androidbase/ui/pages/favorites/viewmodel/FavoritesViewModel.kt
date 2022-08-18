package com.myusufcil.androidbase.ui.pages.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myusufcil.androidbase.ui.pages.favorites.repository.FavoritesRepository
import com.myusufcil.component.ui.stationdata.StationDataDTO
import com.myusufcil.core.base.BaseViewModel
import com.myusufcil.core.database.model.StationsEntity
import com.myusufcil.core.enum.PageStatus
import com.myusufcil.core.enum.SharedPref
import com.myusufcil.core.helpers.LocalPrefManager
import com.myusufcil.core.networking.DataFetchResult
import com.myusufcil.core.recyclerview.DisplayItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : BaseViewModel() {

    val itemPublishSubject = PublishSubject.create<List<DisplayItem>>()

    @Inject
    lateinit var localPrefManager: LocalPrefManager

    private var _updateRecyclerItemLD = MutableLiveData<Pair<Int, StationDataDTO>>()
    val updateRecyclerItemLD: LiveData<Pair<Int, StationDataDTO>>
        get() {
            return _updateRecyclerItemLD
        }

    fun insertDeleteFav(isFav: Boolean, id: Int, index: Int, list: List<DisplayItem>) =
        viewModelScope.launch {
            repository.insertDeleteFav(isFav, id)
            _updateRecyclerItemLD.value = Pair(index, (list[index] as StationDataDTO))
        }

    fun getFavList() = viewModelScope.launch {
        repository.getFavList().collect { _response ->
            when (_response) {
                is DataFetchResult.Success -> {
                    consumeData(_response.data)
                }
                is DataFetchResult.Failure -> {}
                is DataFetchResult.Progress -> {}
            }
        }
    }

    private fun consumeData(favList: List<StationsEntity>) =
        favList.map { _station ->
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
                pageInfo = PageStatus.FAVORITES_PAGE.value,
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

}