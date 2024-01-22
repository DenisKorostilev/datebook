package com.example.datebook.presentation

import com.example.datebook.domain.BaseMapper
import com.example.datebook.domain.Event
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventMapper : BaseMapper<Event, EventUI> {
    override fun map(from: Event): EventUI {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))
        val dateStartFormatted = from.dateStart.toJavaLocalDateTime().format(dateTimeFormatter)
        val dateFinishFormatted = from.dateFinish.toJavaLocalDateTime().format(dateTimeFormatter)
        return EventUI(
            id = from.id,
            timeStart = dateStartFormatted,
            timeFinish = dateFinishFormatted,
            date = from.date,
            name = from.name,
            description = from.description
        )
    }
}
