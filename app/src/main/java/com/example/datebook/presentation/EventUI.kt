package com.example.datebook.presentation

import android.os.Parcelable
import com.example.datebook.presentation.recycler.ListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventUI(
    var id: String,
    var timeStart: String,
    var timeFinish: String,
    var name: String,
    var description: String,
    var date: String
) : ListItem, Parcelable {
    override val itemId: String = id
}
