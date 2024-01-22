package com.example.datebook.domain

interface EventInteractor {
    suspend fun getEventsData(date: String): NetworkResult<List<Event>>
}
