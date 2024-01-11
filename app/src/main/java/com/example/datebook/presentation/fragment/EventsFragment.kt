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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val eventsViewModel: EventsViewModel by viewModel()
    private val adapterDelegate = MainAdapter { id -> moveToEventsDetailsFragment(id) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapterDelegate
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsViewModel.currentEvents.collect { events ->
                    adapterDelegate.items = events
                }
            }
        }
    }

    private fun moveToEventsDetailsFragment(id: String) {
        findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventsDetailsFragment())
    }
}
