package com.myusufcil.androidbase.ui.pages.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidbase.R
import androidbase.databinding.FragmentFavoritesBinding
import androidx.fragment.app.viewModels
import com.myusufcil.androidbase.ui.pages.favorites.viewmodel.FavoritesViewModel
import com.myusufcil.component.SpaceChallangeAdapter
import com.myusufcil.component.ui.stationdata.StationDataDTO
import com.myusufcil.core.base.BaseFragment
import com.myusufcil.core.extension.gone
import com.myusufcil.core.extension.setup
import com.myusufcil.core.extension.toast
import com.myusufcil.core.extension.visible
import com.myusufcil.core.recyclerview.DisplayItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val favoritesList = mutableListOf<DisplayItem>()

    @Inject
    lateinit var adapter: SpaceChallangeAdapter

    override val viewModel: FavoritesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFavList?.setup(adapter.getAdapter())
        bindObserver()
        clicksActions()
    }

    private fun clicksActions() {
        adapter.getAdapter().itemViewClickListener = { view, item, position ->
            when (item) {
                is StationDataDTO -> {
                    if (view.id == R.id.ibFav) {
                        item.isFav = !item.isFav
                        viewModel.insertDeleteFav(
                            isFav = item.isFav,
                            id = item.id,
                            index = position,
                            list =favoritesList
                        )
                    } else if (view.id == R.id.btnTravel) {
                        context?.toast("btnTravel")
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun bindObserver() {
        viewModel.getFavList()
        viewModel.itemPublishSubject.subscribe { _displayItemList ->
            favoritesList.clear()
            favoritesList.addAll(_displayItemList)
            if (favoritesList.isNullOrEmpty()) {
                binding?.llWarningContent?.visible()
                binding?.rvFavList?.gone()
            } else {
                binding?.rvFavList?.visible()
                binding?.llWarningContent?.gone()
            }
            adapter.getAdapter().updateAllItems(favoritesList)
        }

        viewModel.updateRecyclerItemLD.observe(viewLifecycleOwner) {
            if (it.second.isFav) {
                adapter.getAdapter().items[it.first] = it.second
                adapter.getAdapter().notifyItemChanged(it.first)
            } else {
                favoritesList.removeAt(it.first)
                adapter.getAdapter().updateAllItems(favoritesList)
                if (favoritesList.isNullOrEmpty()) {
                    binding?.rvFavList?.gone()
                    binding?.llWarningContent?.visible()
                } else {
                    binding?.rvFavList?.visible()
                    binding?.llWarningContent?.gone()
                }
            }
        }
    }

}