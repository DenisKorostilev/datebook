package com.example.datebook.presentation.recycler

import com.example.datebook.presentation.EventUI
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MainAdapter(
    clickListener: (EventUI) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(DiffUtils()) {

    init {
        delegatesManager
            .addDelegate(MainDelegates.eventItemsDelegates(clickListener))
    }
}
