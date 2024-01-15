package com.example.datebook.data.repository

import com.example.datebook.domain.Event
import com.example.datebook.presentation.EventUI

interface LocalRepository {
    suspend fun readEvents(): List<Event>
    suspend fun cacheEvents(events: List<Event>)
    suspend fun updateEvents(events: List<EventUI>)
}
