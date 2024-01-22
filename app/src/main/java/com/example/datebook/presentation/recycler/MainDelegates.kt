package com.example.datebook.presentation.recycler

import com.example.datebook.databinding.EventItemBinding
import com.example.datebook.presentation.EventUI
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainDelegates {
    fun eventItemsDelegates(itemClickedListener: (EventUI) -> Unit) = adapterDelegateViewBinding<EventUI, ListItem, EventItemBinding>(
        { inflater, container -> EventItemBinding.inflate(inflater, container, false) }
    ) {
        with(binding) {
            root.setOnClickListener { itemClickedListener(item) }
            bind {
                dateStart.text = item.timeStart
                dateFinish.text = item.timeFinish
                date.text = item.date
                name.text = item.name
            }
        }
    }
}
