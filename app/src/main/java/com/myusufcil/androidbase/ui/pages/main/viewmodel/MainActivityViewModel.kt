package com.myusufcil.androidbase.ui.pages.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.myusufcil.androidbase.ui.pages.main.repository.MainRepository
import com.myusufcil.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    fun deleteAll() =
        viewModelScope.launch {
            mainRepository.deleteAll()
        }
}