package com.example.datebook.data.repository

import com.example.datebook.data.room.EventDao
import com.example.datebook.data.room.EventEntity
import com.example.datebook.domain.Event
import com.example.datebook.presentation.EventUI
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocalRepositoryImpl(
    private val eventDao: EventDao
) : LocalRepository {
    override suspend fun readEvents(date: String): List<Event> {
        return eventDao.readEvents().filter { entity ->
            entity.date == date
        }.map { entity ->
            Event(
                id = entity.id,
                dateStart = Instant.fromEpochSeconds(entity.dateStart).toLocalDateTime(TimeZone.UTC),
                dateFinish = Instant.fromEpochSeconds(entity.dateFinish).toLocalDateTime(TimeZone.UTC),
                name = entity.name,
                description = entity.description,
                date = entity.date
            )
        }
    }

    override suspend fun cacheEvents(events: List<Event>) {
        val entities = events.map { event ->
            EventEntity(
                id = event.id,
                dateStart = event.dateStart.toInstant(TimeZone.UTC).epochSeconds,
                dateFinish = event.dateFinish.toInstant(TimeZone.UTC).epochSeconds,
                name = event.name,
                description = event.description,
                date = event.date
            )
        }.toTypedArray()
        eventDao.cacheEvents(events = entities)
    }

    override suspend fun updateEvents(events: List<EventUI>) {
        val entities = events.map { event ->
            EventEntity(
                id = event.id,
                dateStart = LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.parse(
                        event.timeStart,
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                ).atZone(ZoneId.systemDefault()).toInstant().epochSecond,
                dateFinish = LocalDateTime.of(LocalDate.now(), LocalTime.parse(event.timeFinish, DateTimeFormatter.ofPattern("HH:mm")))
                    .atZone(ZoneId.systemDefault()).toInstant().epochSecond,
                name = event.name,
                description = event.description,
                date = event.date
            )
        }.toTypedArray()
        eventDao.updateEvents(events = entities)
    }

    override suspend fun addEvent(event: EventUI) {
        val entities = EventEntity(
            id = event.id,
            dateStart = LocalDateTime.of(LocalDate.now(), LocalTime.parse(event.timeStart, DateTimeFormatter.ofPattern("HH:mm")))
                .atZone(ZoneId.systemDefault()).toInstant().epochSecond,
            name = event.name,
            description = event.description,
            dateFinish = LocalDateTime.of(LocalDate.now(), LocalTime.parse(event.timeFinish, DateTimeFormatter.ofPattern("HH:mm")))
                .atZone(ZoneId.systemDefault()).toInstant().epochSecond,
            date = event.date
        )
        eventDao.addEvent(event = entities)
    }

    override suspend fun deleteEvent(event: EventUI) {
        val entities = EventEntity(
            id = event.id,
            dateStart = LocalDateTime.of(LocalDate.now(), LocalTime.parse(event.timeStart, DateTimeFormatter.ofPattern("HH:mm")))
                .atZone(ZoneId.systemDefault()).toInstant().epochSecond,
            dateFinish = LocalDateTime.of(LocalDate.now(), LocalTime.parse(event.timeFinish, DateTimeFormatter.ofPattern("HH:mm")))
                .atZone(ZoneId.systemDefault()).toInstant().epochSecond,
            date = event.date,
            name = event.name,
            description = event.description
        )
        eventDao.deleteEvent(event = entities)
    }
}
