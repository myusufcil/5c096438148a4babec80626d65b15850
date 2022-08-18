package com.myusufcil.component.ui.stationdata

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.myusufcil.component.R
import com.myusufcil.component.databinding.ItemStationsDatasBinding
import com.myusufcil.core.constants.AppConstants
import com.myusufcil.core.enum.PageStatus
import com.myusufcil.core.enum.SharedPref
import com.myusufcil.core.extension.gone
import com.myusufcil.core.extension.visible
import com.myusufcil.core.helpers.LocalPrefManager
import com.myusufcil.core.recyclerview.DisplayItem
import com.myusufcil.core.recyclerview.ViewHolder
import com.myusufcil.core.recyclerview.ViewHolderBinder
import com.myusufcil.core.recyclerview.ViewHolderFactory
import javax.inject.Inject
import kotlin.math.roundToInt


class StationDataViewHolder @Inject constructor(var binding: ItemStationsDatasBinding) :
    ViewHolder<StationDataDTO>(binding) {

    lateinit var localPrefManager: LocalPrefManager

    override fun bind(item: StationDataDTO) {
        val shared =
            binding.root.context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
        localPrefManager = LocalPrefManager(shared)
        val x1 = localPrefManager.pullString(SharedPref.PREF_X1.value, item.coordinateX.toString())
            .toDouble()
        val y1 = localPrefManager.pullString(SharedPref.PREF_Y1.value, item.coordinateX.toString())
            .toDouble()
        with(binding) {
            when (item.pageInfo) {
                PageStatus.STATIONS_PAGE.value -> {
                    if (x1 == item.coordinateX && y1 == item.coordinateY) {
                        clRootView.background = ContextCompat.getDrawable(
                            root.context,
                            R.drawable.bg_item_stations_root_selected_radius
                        )
                        btnTravel.gone()
                    } else {
                        clRootView.background = ContextCompat.getDrawable(
                            root.context,
                            R.drawable.bg_item_stations_root_radius
                        )
                        btnTravel.visible()
                    }
                    if (item.isTaskDone) {
                        btnTravel.gone()
                    }
                    ibFav.visible()
                    btnTravel.setOnClickListener {
                        itemViewClickListener?.invoke(it, item, bindingAdapterPosition)
                    }
                }
                PageStatus.FAVORITES_PAGE.value -> {
                    clRootView.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.bg_item_stations_root_radius
                    )
                    btnTravel.gone()
                    ibFav.visible()
                }
            }

            if (item.name == root.context.getString(R.string.earth)) {
                btnTravel.gone()
                ibFav.gone()
            }
            ibFav.setOnClickListener {
                itemViewClickListener?.invoke(it, item, bindingAdapterPosition)
                loadImageFavStatus(item.isFav)
            }
            loadImageFavStatus(item.isFav)
            tvEusCount.text = "${(item.eus?.times(100.0))?.roundToInt()?.div(100.0)} - EUS "
            tvStationNameLastEusCount.text = "${item.name}"
            tvNeedStock.text = "${item.need}/${item.stock}"
        }
    }

    private fun loadImageFavStatus(isFav: Boolean) {
        isFav.takeIf { it }?.let {
            binding.ibFav.setImageResource(R.drawable.ic_star_filled)
        } ?: let {
            binding.ibFav.setImageResource(R.drawable.ic_star)
        }
    }

    class HolderFactory @Inject constructor() : ViewHolderFactory {
        override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            StationDataViewHolder(
                ItemStationsDatasBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }

    class BinderFactory @Inject constructor() : ViewHolderBinder {
        override fun bind(holder: RecyclerView.ViewHolder, item: DisplayItem) {
            (holder as StationDataViewHolder).bind(item as StationDataDTO)
        }
    }

}