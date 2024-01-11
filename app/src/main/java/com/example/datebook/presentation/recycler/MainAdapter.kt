package com.example.datebook.presentation.recycler

import com.example.datebook.presentation.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MainAdapter(
    clickListener: (String) -> Unit,
) : AsyncListDifferDelegationAdapter<ListItem>(DiffUtils()) {

    init {
        delegatesManager
            .addDelegate(MainDelegates.eventsItemsDelegates(clickListener))
    }
}