package com.example.datebook.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsFragmentBinding
import com.example.datebook.presentation.recycler.MainAdapter
import com.example.datebook.presentation.viewmodel.EventsViewModel
import com.example.datebook.presentation.viewmodel.NavigationEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Calendar

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val eventsViewModel: EventsViewModel by sharedViewModel()
    private val adapterDelegate = MainAdapter { eventUI -> eventsViewModel.onEventClicked(eventUI) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.adapter = adapterDelegate
            calendar.setOnDateChangeListener { _, year: Int, month: Int, dayOfMonth: Int ->
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                eventsViewModel.setCurrentDate(calendar.timeInMillis)
                eventsViewModel.receiveEvents()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsViewModel.currentEvents.collect { events ->
                    adapterDelegate.items = events
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsViewModel.currentDate.collect { date ->
                    binding.calendar.date = date
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsViewModel.navigationItem.collect { item ->
                    when (item) {
                        is NavigationEvent.Details -> moveToEventsDetailsFragment(item)
                        is NavigationEvent.Back -> findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun moveToEventsDetailsFragment(navigation: NavigationEvent.Details) {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToEventsDetailFragment(
                navigation.eventUI,
                navigation.date,
            ),
        )
    }
}
