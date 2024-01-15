package com.example.datebook.data

import com.example.datebook.domain.BaseMapper
import com.example.datebook.domain.Event
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EventDTOMapper : BaseMapper<EventDTO, Event> {
    override fun map(from: EventDTO): Event {
        return Event(
            id = from.id,
            dateStart = Instant.fromEpochSeconds(from.dateStart).toLocalDateTime(TimeZone.UTC),
            dateFinish = Instant.fromEpochSeconds(from.dateFinish).toLocalDateTime(TimeZone.UTC),
            name = from.name,
            description = from.description
        )
    }
}
