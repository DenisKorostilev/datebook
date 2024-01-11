package com.example.datebook.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsDetailFragmentBinding

class EventsDetailFragment : Fragment(R.layout.events_detail_fragment) {
    private val binding: EventsDetailFragmentBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.revert.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
