package com.example.datebook.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventUI(
    val id: String,
    val dateStart: String,
    val dateFinish: String,
    val name: String,
    val description: String
) : ListItem, Parcelable {
    override val itemId: String = id
}
