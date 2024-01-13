package com.example.datebook.domain

interface EventInteractor {
    suspend fun getEventsData(): NetworkResult<List<Event>>
}
