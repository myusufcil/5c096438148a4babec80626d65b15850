package com.myusufcil.core.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ViewHolder<T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    var itemViewClickListener: ((view: View, item: DisplayItem, position: Int) -> Unit)? = null
    abstract fun bind(item: T)
}