package com.example.datebook.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsFragmentBinding
import com.example.datebook.presentation.launchRepeatedly
import com.example.datebook.presentation.recycler.MainAdapter
import com.example.datebook.presentation.viewmodel.EventsViewModel
import com.example.datebook.presentation.viewmodel.NavigationEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Calendar

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val eventsViewModel: EventsViewModel by sharedViewModel()
    private val adapterDelegate = MainAdapter { eventUI -> eventsViewModel.onEventClicked(eventUI) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        calendarInteraction()
        onButtonClick()
    }

    private fun bindViews() {
        binding.recyclerView.adapter = adapterDelegate
        viewLifecycleOwner.launchRepeatedly {
            eventsViewModel.currentEvents.onEach { events ->
                adapterDelegate.items = events
            }.launchIn(this)

            eventsViewModel.currentDate.onEach { date ->
                binding.calendar.date = date
            }.launchIn(this)

            eventsViewModel.navigationItem.onEach { item ->
                when (item) {
                    is NavigationEvent.Details -> moveToEventsDetailFragment(item)
                    is NavigationEvent.Back -> findNavController().popBackStack()
                }
            }.launchIn(this)
        }
    }

    private fun onButtonClick() {
        binding.addEvent.setOnClickListener {
            moveToAddEventFragment()
        }
    }

    private fun calendarInteraction() {
        binding.calendar.setOnDateChangeListener { _, year: Int, month: Int, dayOfMonth: Int ->
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            eventsViewModel.setCurrentDate(calendar.timeInMillis)
        }
    }

    private fun moveToEventsDetailFragment(navigation: NavigationEvent.Details) {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToEventsDetailFragment(
                navigation.eventUI
            )
        )
    }

    private fun moveToAddEventFragment() {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToCreateEventsFragment()
        )
    }
}
