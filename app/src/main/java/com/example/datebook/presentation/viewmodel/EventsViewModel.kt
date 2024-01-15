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

sealed interface NavigationEvent {
    data class Details(val eventUI: EventUI, val date: Long) : NavigationEvent
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
        receiveEvents()
    }

    fun receiveEvents() {
        _currentEvents.value = emptyList()
        viewModelScope.launch {
            when (val networkResult = eventsInteractor.getEventsData()) {
                is NetworkResult.Success -> {
                    allEvents = networkResult.result.map { mapper.map(it) }
                    _currentEvents.value = allEvents
                }

                is NetworkResult.Error -> {}
            }
        }
    }

    fun onMarkClicked() {
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
        viewModelScope.launch {
            _navigationItem.emit(NavigationEvent.Back)
            localRepositoryImpl.updateEvents(changedEvents)
        }
    }

    fun onEventClicked(eventUI: EventUI) {
        viewModelScope.launch {
            _navigationItem.emit(NavigationEvent.Details(eventUI, _currentDate.value))
            _currentEvent.value = eventUI
        }
    }

    fun setCurrentDate(date: Long) {
        _currentDate.value = date
    }

    fun updateTitleText(text: String) {
        _currentEvent.value = _currentEvent.value?.copy(name = text)
    }

    fun updateDescription(text: String) {
        _currentEvent.value = _currentEvent.value?.copy(description = text)
    }
}
