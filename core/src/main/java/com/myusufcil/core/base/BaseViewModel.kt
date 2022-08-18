package com.myusufcil.core.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlin.math.pow
import kotlin.math.sqrt

abstract class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    @CallSuper
    override fun onCleared() = disposables.dispose()

    open fun handleIntent(intent: Intent) {}
    open fun handleArguments(argument: Bundle) {}

    fun distanceBetweenTwoPoints(x1: Double, y1: Double, x2: Double, y2: Double): Double =
        sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))

}