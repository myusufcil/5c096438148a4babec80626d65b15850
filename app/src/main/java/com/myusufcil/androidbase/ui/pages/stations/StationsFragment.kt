package com.myusufcil.androidbase.ui.pages.stations

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidbase.R
import androidbase.databinding.FragmentStationsBinding
import androidx.fragment.app.viewModels
import com.myusufcil.androidbase.ui.pages.stations.viewmodel.StationsFragmentViewModel
import com.myusufcil.component.SpaceChallangeAdapter
import com.myusufcil.component.ui.stationdata.StationDataDTO
import com.myusufcil.core.base.BaseFragment
import com.myusufcil.core.enum.ErrorMessages
import com.myusufcil.core.enum.SharedPref
import com.myusufcil.core.extension.*
import com.myusufcil.core.helpers.LocalPrefManager
import com.myusufcil.core.recyclerview.DisplayItem
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class StationsFragment :
    BaseFragment<StationsFragmentViewModel, FragmentStationsBinding>(FragmentStationsBinding::inflate) {

    override val viewModel: StationsFragmentViewModel by viewModels()

    private val stationList = mutableListOf<DisplayItem>()
    private val filterPageList = mutableListOf<DisplayItem>()

    var countDownTimer: CountDownTimer? = null

    @Inject
    lateinit var localPrefManager: LocalPrefManager

    @Inject
    lateinit var adapter: SpaceChallangeAdapter

    private var isErrorOccured = false
    private var errorType = ""

    private var UGS = 0
    private var EUS = 0.0
    private var DS = 0
    private var HK = 0
    private var dsAverageTime = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvStationPage?.setup(adapter.getAdapter())
        setupUI()
        bindObserver()
        clicksActions()
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(DS.toLong() * 10, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.d("time is going" + (millisUntilFinished / 1000).toString())
                if ((millisUntilFinished.div(1000)).mod(DS / 1000) == 0) {
                    val hk = localPrefManager.pullInt(SharedPref.PREF_HK.value, 0)
                    localPrefManager.pushInt(
                        SharedPref.PREF_HK.value,
                        hk.takeIf { it != 0 }?.minus(10) ?: 0
                    )
                    setupUI()
                }
            }

            override fun onFinish() {
                isErrorOccured = true
                errorType = ErrorMessages.DurabilityNotEnough.errorMessage
                sendShipHome()
                setupUI()
                context?.toast(ErrorMessages.DurabilityNotEnough.errorMessage)
                adapter.getAdapter().notifyDataSetChanged()
            }
        }.start()
    }


    private fun bindObserver() {
        viewModel.getStatusOfTask()
        viewModel.getStationsFromLocal()
        viewModel.itemPublishSubject.subscribe { _displayItemList ->
            if (_displayItemList.isNullOrEmpty()) {
                viewModel.getStationFromApi()
            } else {
                stationList.clear()
                stationList.addAll(_displayItemList)
                filterPageList.clear()
                filterPageList.addAll(_displayItemList)
                adapter.getAdapter().updateAllItems(stationList)
            }
        }.isDisposed
        viewModel.updateRecyclerItemLD.observe(viewLifecycleOwner) {
            adapter.getAdapter().items[it.first] = it.second
            adapter.getAdapter().notifyItemChanged(it.first)
        }
        viewModel.taskStatusLiveData.observe(viewLifecycleOwner) {
            if (it.all { it } && it.isNotEmpty()) {
                sendShipHome()
                context?.toastShort("Bütün kargolar başarıyla iletildi, dünyaya geri döndünüz.")
                countDownTimer?.cancel()
                adapter.getAdapter().notifyDataSetChanged()
            }
        }
    }

    private fun setupUI() {
        binding?.etSearchView?.addTextChangedListener(searchWatcher)
        UGS = localPrefManager.pullInt(SharedPref.PREF_UGS.value, 0)
        EUS = localPrefManager.pullString(SharedPref.PREF_EUS.value, "").toDouble()
        DS = localPrefManager.pullInt(SharedPref.PREF_DS.value, 0)
        HK = localPrefManager.pullInt(SharedPref.PREF_HK.value, 100)
        dsAverageTime = DS / 1000
        updateSpaceShipValues()
    }

    private fun updateSpaceShipValues() {
        val df = DecimalFormat("#.##")
        val eus = df.format(EUS).replace(",", ".")
        binding?.let {
            with(it) {
                tvUgs.text = "UGS : $UGS"
                tvEus.text = "EUS : $eus"
                tvDs.text = "DS : $DS"
                tvTime.text = "Süre : ${dsAverageTime}s"
                tvDamageCapacity.text = "Hasar Kapasitesi : $HK"
                tvSpaceShipName.text = localPrefManager.pullString(SharedPref.PREF_SSN.value, "")
            }
        }
    }

    private fun clicksActions() {
        adapter.getAdapter().itemViewClickListener = { view, item, position ->
            when (item) {
                is StationDataDTO -> {
                    if (view.id == com.myusufcil.component.R.id.ibFav) {
                        item.isFav = !item.isFav
                        viewModel.insertDeleteFav(item.isFav, item.id, position, stationList)
                    } else if (view.id == R.id.btnTravel) {
                        val distanceBetweenTwoPlanet = viewModel.distanceBetweenTwoPoints(
                            x1 = localPrefManager.pullString(SharedPref.PREF_X1.value, "0.0")
                                .toDouble(),
                            y1 = localPrefManager.pullString(SharedPref.PREF_Y1.value, "0.0")
                                .toDouble(),
                            x2 = item.coordinateX ?: 0.0,
                            y2 = item.coordinateY ?: 0.0
                        )
                        UGS = UGS.minus(item.need ?: 0)

                        if (EUS < distanceBetweenTwoPlanet && !isErrorOccured) {
                            isErrorOccured = true
                            errorType = ErrorMessages.EUSNotEnough.errorMessage
                        }
                        if (UGS < 0) {
                            errorType = ErrorMessages.UGSNotEnough.errorMessage
                            isErrorOccured = true
                            item.need = abs(UGS)
                            UGS = 0
                            localPrefManager.pushInt(SharedPref.PREF_UGS.value, UGS)
                        }

                        //UGS, HK, EUS HANDLE
                        if (!isErrorOccured) {
                            item.stock = item.stock?.plus(item.need ?: 0)
                            item.need = 0
                            EUS -= distanceBetweenTwoPlanet
                            localPrefManager.pushInt(SharedPref.PREF_UGS.value, UGS)
                            localPrefManager.pushString(SharedPref.PREF_EUS.value, EUS.toString())
                            localPrefManager.pushString(
                                SharedPref.PREF_X1.value,
                                item.coordinateX.toString()
                            )
                            localPrefManager.pushString(
                                SharedPref.PREF_Y1.value,
                                item.coordinateY.toString()
                            )
                            item.isTaskDone = true
                        } else {
                            when (errorType) {
                                ErrorMessages.DurabilityNotEnough.errorMessage -> {
                                    context?.toast(errorType)
                                }
                                ErrorMessages.UGSNotEnough.errorMessage -> {
                                    context?.toast(errorType)
                                }
                                ErrorMessages.EUSNotEnough.errorMessage -> {
                                    context?.toast(errorType)
                                }
                                ErrorMessages.TimeNotEnough.errorMessage -> {
                                    context?.toast(errorType)
                                }
                            }
                            sendShipHome()
                        }
                        viewModel.travelUpdateUI(
                            id = item.id,
                            item = item
                        )
                        setupUI()
                    }
                }
            }
        }
    }

    private val searchWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (!binding?.etSearchView?.text.isNullOrEmpty()) {
                stationList.filter {
                    (it as StationDataDTO).name?.lowercase(Locale.getDefault())!!.contains(
                        binding?.etSearchView?.text.toString().lowercase(Locale.getDefault())
                    )
                }.let {
                    if (!it.isNullOrEmpty()) {
                        filterPageList.clear()
                        filterPageList.addAll(it)
                        adapter.getAdapter().updateAllItems(filterPageList)
                        showRecyclerView()
                    } else {
                        showNotContentLayout()
                    }
                }
            } else {
                binding?.let {
                    with(it) {
                        adapter.getAdapter()
                            .updateAllItems(stationList)
                        showRecyclerView()
                    }
                }
            }
        }
    }

    private fun showRecyclerView() {
        binding?.let {
            with(it) {
                llNoResult.gone()
                rvStationPage.visible()
            }
        }
    }

    private fun showNotContentLayout() {
        binding?.let {
            with(it) {
                rvStationPage.gone()
                llNoResult.visible()
            }
        }
    }

    private fun sendShipHome() {
        val position = stationList.indexOfFirst {
            it is StationDataDTO || (it as StationDataDTO).name == getString(R.string.earth)
        }.takeIf { it != -1 }
        binding?.rvStationPage?.smoothScrollToPosition(position ?: 0)
        localPrefManager.pushString(SharedPref.PREF_X1.value, "0.0")
        localPrefManager.pushString(SharedPref.PREF_Y1.value, "0.0")
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
    }

}