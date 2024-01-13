package com.example.datebook.data.repository

import com.example.datebook.domain.Event
import com.example.datebook.domain.NetworkResult

interface EventRepository {
    suspend fun getEventsData(): NetworkResult<List<Event>>
}
