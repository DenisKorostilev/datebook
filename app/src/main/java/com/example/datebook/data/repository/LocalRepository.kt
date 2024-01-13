package com.example.datebook.data.repository

import com.example.datebook.domain.Event

interface LocalRepository {
    suspend fun readEvents(): List<Event>
    suspend fun cacheEvents(events: List<Event>)
}
