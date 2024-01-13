package com.example.datebook.data.repository

import com.example.datebook.data.EventDTOMapper
import com.example.datebook.data.EventsApi
import com.example.datebook.domain.Event
import com.example.datebook.domain.NetworkResult

class EventMockRepository(
    private val eventsApi: EventsApi,
    private val mapper: EventDTOMapper,
) : EventRepository {
    override suspend fun getEventsData(): NetworkResult<List<Event>> {
        val result = eventsApi.getEventsResults()
        if (result.isSuccessful) {
            val body = result.body()
            if (body != null) {
                val events: List<Event> = body.map { mapper.map(it) }
                return NetworkResult.Success(events)
            }
        }
        return NetworkResult.Error("Ошибка соединения с сервером")
    }
}
