package com.example.datebook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datebook.data.EventRepository
import com.example.datebook.domain.NetworkResult
import com.example.datebook.presentation.EventMapper
import com.example.datebook.presentation.EventUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventsViewModel(
    private val eventsRepository: EventRepository,
    private val mapper: EventMapper,
) : ViewModel() {
    private var allEvents: List<EventUI> = emptyList()
    private var _currentEvents = MutableStateFlow<List<EventUI>>(emptyList())
    val currentEvents: StateFlow<List<EventUI>> = _currentEvents.asStateFlow()

    init {
        receiveEvents()
    }

    private fun receiveEvents() {
        _currentEvents.value = emptyList()
        viewModelScope.launch {
            when (val networkResult = eventsRepository.getEventsData()) {
                is NetworkResult.Success -> {
                    allEvents = networkResult.result.map { mapper.map(it) }
                    _currentEvents.value = allEvents
                }

                is NetworkResult.Error -> {}
            }
        }
    }
}
