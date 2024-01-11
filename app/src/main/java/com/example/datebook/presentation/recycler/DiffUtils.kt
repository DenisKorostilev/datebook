package com.example.datebook.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.datebook.presentation.ListItem

class DiffUtils : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.itemId == newItem.itemId

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.equals(newItem)
}