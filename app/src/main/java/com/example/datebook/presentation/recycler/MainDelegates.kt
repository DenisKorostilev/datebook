package com.example.datebook.presentation.recycler

import com.example.datebook.databinding.EventItemBinding
import com.example.datebook.presentation.EventUI
import com.example.datebook.presentation.ListItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainDelegates {
    fun eventsItemsDelegates(itemClickedListener: (String) -> Unit) = adapterDelegateViewBinding<EventUI, ListItem, EventItemBinding>(
        { inflater, container -> EventItemBinding.inflate(inflater, container, false) },
    ) {
        with(binding) {
            root.setOnClickListener { itemClickedListener(item.id) }
            bind {
                dateStart.text = item.dateStart
                dateFinish.text = item.dateFinish
                name.text = item.name
            }
        }
    }
}
