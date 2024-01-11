package com.example.datebook.presentation

data class EventUI(
    val id: String,
    val dateStart: String,
    val dateFinish: String,
    val name: String,
    val description: String,
) : ListItem {
    override val itemId: String = id
}