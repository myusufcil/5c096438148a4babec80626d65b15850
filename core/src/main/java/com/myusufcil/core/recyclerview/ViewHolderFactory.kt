package com.myusufcil.core.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderFactory {
    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
}