package com.example.datebook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datebook.data.repository.LocalRepositoryImpl
import com.example.datebook.domain.EventInteractor
import com.example.datebook.domain.NetworkResult
import com.example.datebook.presentation.EventMapper
import com.example.datebook.presentation.EventUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed interface NavigationEvent {
    data class Details(val eventUI: EventUI) : NavigationEvent
    data object Back : NavigationEvent
}

class EventsViewModel(
    private val eventsInteractor: EventInteractor,
    private val mapper: EventMapper,
    private val localRepositoryImpl: LocalRepositoryImpl
) : ViewModel() {
    private var allEvents: List<EventUI> = emptyList()

    private val _currentEvents = MutableStateFlow<List<EventUI>>(emptyList())
    val currentEvents: StateFlow<List<EventUI>> = _currentEvents.asStateFlow()

    private val _currentDate = MutableStateFlow(System.currentTimeMillis())
    val currentDate: StateFlow<Long> = _currentDate.asStateFlow()

    private val _navigationItem = MutableSharedFlow<NavigationEvent>()
    val navigationItem: SharedFlow<NavigationEvent> = _navigationItem.asSharedFlow()

    private val _currentEvent: MutableStateFlow<EventUI?> = MutableStateFlow(null)

    init {
        val date = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru")))
        receiveEvents(date)
    }

    private fun receiveEvents(date: String) {
        _currentEvents.value = emptyList()
        viewModelScope.launch {
            when (val networkResult = eventsInteractor.getEventsData(date)) {
                is NetworkResult.Success -> {
                    allEvents = networkResult.result.map { mapper.map(it) }
                    _currentEvents.value = allEvents
                }
                is NetworkResult.Error -> {}
            }
        }
    }

    fun onBtnDeleteClick() {
        viewModelScope.launch {
            val currentEvent = _currentEvent.value
            currentEvent?.let {
                localRepositoryImpl.deleteEvent(currentEvent)
                receiveEvents(currentEvent.date)
                _navigationItem.emit(NavigationEvent.Back)
            }
        }
    }

    fun onEventClicked(eventUI: EventUI) {
        viewModelScope.launch {
            _navigationItem.emit(NavigationEvent.Details(eventUI))
            _currentEvent.value = eventUI
        }
    }

    fun backStack() {
        viewModelScope.launch {
            _navigationItem.emit(NavigationEvent.Back)
        }
    }

    fun onMarkClicked() {
        val dateString = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru")))
        viewModelScope.launch {
            val currentEvent = _currentEvent.value
            val currentEvents = _currentEvents.value
            val changedEvents = currentEvents.map { event ->
                if (event.id == currentEvent?.id) {
                    currentEvent
                } else {
                    event
                }
            }
            _currentEvent.value = null
            _currentEvents.value = changedEvents
            _navigationItem.emit(NavigationEvent.Back)
            localRepositoryImpl.updateEvents(changedEvents)
            receiveEvents(dateString)
        }
    }

    fun addEvent(event: EventUI) {
        _currentEvent.value = event
        val dateString = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru")))
        viewModelScope.launch {
            event.let {
                val currentEvent = _currentEvent.value
                val currentEvents = _currentEvents.value
                val changedEvents = currentEvents.map { event ->
                    if (event.id == currentEvent?.id) {
                        currentEvent
                    } else {
                        event
                    }
                }
                _currentEvents.value = changedEvents
            }
            localRepositoryImpl.addEvent(event)
            receiveEvents(dateString)
            backStack()
        }
    }

    fun setCurrentDate(date: Long) {
        _currentDate.value = date
        val dateString = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru")))
        receiveEvents(dateString)
    }

    fun updateDetailTitleTask(textName: String) {
        _currentEvent.value = _currentEvent.value?.copy(name = textName)
    }

    fun updateDate(date: String) {
        _currentEvent.value = _currentEvent.value?.copy(date = date)
    }

    fun updateStartTime(startTime: String) {
        _currentEvent.value = _currentEvent.value?.copy(timeStart = startTime)
    }

    fun updateEndTime(endTime: String) {
        _currentEvent.value = _currentEvent.value?.copy(timeFinish = endTime)
    }

    fun updateDetailDescription(textDescription: String) {
        _currentEvent.value = _currentEvent.value?.copy(description = textDescription)
    }
}
