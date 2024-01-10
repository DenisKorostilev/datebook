package com.example.datebook.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsFragmentBinding

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
