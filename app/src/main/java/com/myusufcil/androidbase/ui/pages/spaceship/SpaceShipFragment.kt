package com.myusufcil.androidbase.ui.pages.spaceship

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidbase.R
import androidbase.databinding.FragmentSpaceShipBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.myusufcil.core.base.BaseFragment
import com.myusufcil.androidbase.ui.pages.spaceship.viewmodel.SpaceShipViewModel
import com.myusufcil.core.enum.SharedPref
import com.myusufcil.core.enum.WarningMessages
import com.myusufcil.core.helpers.LocalPrefManager
import com.myusufcil.core.extension.invisible
import com.myusufcil.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SpaceShipFragment :
    BaseFragment<SpaceShipViewModel, FragmentSpaceShipBinding>(FragmentSpaceShipBinding::inflate),
    SeekBar.OnSeekBarChangeListener {

    override val viewModel: SpaceShipViewModel by viewModels()

    private var valueDurability = 0
    private var valueSpeed = 0
    private var valueCapacity = 0

    @Inject
    lateinit var localPrefManager: LocalPrefManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openHomeFragment()
        setupUI()
    }

    private fun openHomeFragment() {
        binding?.btnOpenHomeActivity?.setOnClickListener {
            binding?.let {
                with(it) {
                    if (etSpaceShipName.text?.trim().isNullOrEmpty()) {
                        tvWarningMessage.visible()
                        tvWarningMessage.text = WarningMessages.SpaceShipNotnull.value
                    } else if (calculatePoint()) {
                        localPrefManager.pushInt(SharedPref.PREF_UGS.value, (valueCapacity * 10000))
                        localPrefManager.pushString(SharedPref.PREF_EUS.value, (valueSpeed * 20).toString())
                        localPrefManager.pushString(SharedPref.PREF_SSN.value, etSpaceShipName.text.toString())
                        localPrefManager.pushInt(SharedPref.PREF_DS.value, (valueDurability * 10000))
                        localPrefManager.pushInt(SharedPref.PREF_HK.value, 100)
                        localPrefManager.pushString(SharedPref.PREF_X1.value,"0.0")
                        localPrefManager.pushString(SharedPref.PREF_Y1.value,"0.0")
                        findNavController().navigate(R.id.action_spaceShipFragment_to_stationFragment)
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding?.sbDurability?.setOnSeekBarChangeListener(this)
        binding?.sbSpeed?.setOnSeekBarChangeListener(this)
        binding?.sbCapaticy?.setOnSeekBarChangeListener(this)
        binding?.let {
            with(it) {
                valueDurability = sbDurability.progress
                valueSpeed = sbSpeed.progress
                valueCapacity = sbCapaticy.progress

                tvDurabilityValue.text = valueDurability.toString()
                tvSpeedValue.text = valueSpeed.toString()
                tvCapacityValue.text = valueCapacity.toString()
                calculatePoint()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.sbDurability -> {
                binding?.tvDurabilityValue?.text = progress.toString()
                valueDurability = progress
            }
            R.id.sbSpeed -> {
                binding?.tvSpeedValue?.text = progress.toString()
                valueSpeed = progress
            }
            R.id.sbCapaticy -> {
                binding?.tvCapacityValue?.text = progress.toString()
                valueCapacity = progress
            }
        }
        calculatePoint()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(p0: SeekBar?) {}

    private fun calculatePoint(): Boolean {
        val sumPoint = valueDurability + valueSpeed + valueCapacity
        binding?.tvPoint?.text = "Dağıtılacak Puan : $sumPoint"

        return if (valueDurability == 0 || valueSpeed == 0 || valueCapacity == 0) {
            binding?.tvWarningMessage?.visible()
            binding?.tvWarningMessage?.text = WarningMessages.PointValueNotZero.value
            false
        } else if (sumPoint != 15) {
            binding?.tvWarningMessage?.visible()
            binding?.tvWarningMessage?.text = WarningMessages.SumPoint.value
            false
        } else {
            binding?.tvWarningMessage?.invisible()
            true
        }
    }
}