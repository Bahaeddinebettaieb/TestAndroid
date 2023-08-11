package com.example.testandroid.presentation.base

import androidx.recyclerview.widget.RecyclerView
import android.view.View


abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)

}