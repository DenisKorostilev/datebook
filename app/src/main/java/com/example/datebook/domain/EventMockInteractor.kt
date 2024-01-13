package com.example.datebook.domain

import com.example.datebook.data.repository.EventRepository
import com.example.datebook.data.repository.LocalRepository

class EventMockInteractor(
    private val eventRepository: EventRepository,
    private val localRepository: LocalRepository,
) : EventInteractor {
    override suspend fun getEventsData(): NetworkResult<List<Event>> {
        val events = localRepository.readEvents()
        return if (events.isEmpty()) { // если детали события отсутствуют локально, идем в сеть
            val result = eventRepository.getEventsData()
            if (result is NetworkResult.Success) { // в случае успеха кэшируем данные локально
                localRepository.cacheEvents(result.result)
            }
            result // возвращаем результат удаленного запроса
        } else { // если детали события есть локально, не делаем удаленный запрос
            NetworkResult.Success(events)
        }
    }
}
