package com.myusufcil.androidbase.ui.pages.main

import android.os.Bundle
import androidbase.R
import androidbase.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.myusufcil.core.base.BaseActivity
import com.myusufcil.androidbase.ui.pages.main.viewmodel.MainActivityViewModel
import com.myusufcil.core.extension.gone
import com.myusufcil.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding, MainActivityViewModel>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController

    override val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        binding?.navView?.setupWithNavController(navController)
        listenNavController()
    }

    private fun listenNavController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.spaceShipFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding?.navView?.visible()
    }

    private fun hideBottomNav() {
        binding?.navView?.gone()
    }

    override fun onPause() {
        viewModel.deleteAll()
        finish()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.deleteAll()
        super.onDestroy()
    }
}