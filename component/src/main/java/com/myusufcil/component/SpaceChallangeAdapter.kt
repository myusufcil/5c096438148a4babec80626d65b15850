package com.myusufcil.component

import com.myusufcil.component.constants.RecyclerViewAdapterConstants
import com.myusufcil.core.recyclerview.RecyclerViewAdapter

class SpaceChallangeAdapter {

    fun getAdapter() = _adapter

    private val _adapter = RecyclerViewAdapter(
        viewHolderFactoryMap = RecyclerViewAdapterConstants().holderFactoryMap,
        viewBinderFactoryMap = RecyclerViewAdapterConstants().binderFactoryMap
    )
}