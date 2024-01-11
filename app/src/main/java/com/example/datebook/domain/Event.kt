package com.example.datebook.domain

import kotlinx.datetime.LocalDateTime

data class Event(
    val id: String,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime,
    val name: String,
    val description: String,
)
