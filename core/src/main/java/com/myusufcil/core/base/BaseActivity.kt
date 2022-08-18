package com.myusufcil.core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable

typealias Inflater<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>(private val inflate: Inflater<VB>) :
    AppCompatActivity() {

    abstract val viewModel: VM

    private var _binding: VB? = null
    val binding get() = _binding


    private var destroyDisposable: CompositeDisposable? = null
    private var stopDisposable: CompositeDisposable? = null
    private var pauseDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        destroyDisposable = CompositeDisposable()
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding?.root)
        viewModel.handleIntent(intent)
    }

    override fun onStart() {
        super.onStart()

        stopDisposable = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()

        stopDisposable?.dispose()
    }

    override fun onResume() {
        super.onResume()

        pauseDisposable = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()

        pauseDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable?.dispose()
    }
}