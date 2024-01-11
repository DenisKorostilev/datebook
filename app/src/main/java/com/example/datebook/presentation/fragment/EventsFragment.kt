package com.example.datebook.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsFragmentBinding
import com.example.datebook.presentation.recycler.MainAdapter

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val adapterDelegate = MainAdapter { id -> moveToEventsDetailsFragment(id) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapterDelegate
    }

    private fun moveToEventsDetailsFragment(id: String) {
        findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventsDetailsFragment())
    }
}
