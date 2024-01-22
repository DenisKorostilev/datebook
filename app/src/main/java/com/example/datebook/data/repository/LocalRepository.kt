package com.example.datebook.data.repository

import com.example.datebook.domain.Event
import com.example.datebook.presentation.EventUI

interface LocalRepository {
    suspend fun readEvents(date: String): List<Event>
    suspend fun cacheEvents(events: List<Event>)
    suspend fun updateEvents(events: List<EventUI>)
    suspend fun addEvent(event: EventUI)
    suspend fun deleteEvent(event: EventUI)
}
