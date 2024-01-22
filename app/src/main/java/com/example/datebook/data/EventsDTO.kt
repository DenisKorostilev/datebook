package com.example.datebook.data

data class EventsDTO(
    var events: List<EventDTO>
)

data class EventDTO(
    val id: String,
    val dateStart: Long,
    val dateFinish: Long,
    val date: String,
    val name: String,
    val description: String
)
