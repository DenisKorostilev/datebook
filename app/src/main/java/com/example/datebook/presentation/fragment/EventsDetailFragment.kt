package com.example.datebook.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsDetailFragmentBinding
import com.example.datebook.presentation.viewmodel.EventsViewModel
import com.example.datebook.presentation.viewmodel.NavigationEvent
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventsDetailFragment : Fragment(R.layout.events_detail_fragment) {
    private val binding: EventsDetailFragmentBinding by viewBinding()
    private val arguments: EventsDetailFragmentArgs by navArgs()
    private val eventsViewModel: EventsViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        onClick()
        textChanged()
        navigation()
    }

    private fun initViews() {
        val eventUI = arguments.eventUI
        with(binding) {
            titleTask.setText(eventUI.name)
            dateTime.text =
                Instant.fromEpochMilliseconds(arguments.date).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("ru")))
            "Начало - ${eventUI.dateStart}".also { detailsDateStart.text = it }
            "Конец - ${eventUI.dateFinish}".also { detailsDateFinish.text = it }
            description.setText(eventUI.description)
        }
    }

    private fun onClick() {
        with(binding) {
            mark.setOnClickListener {
                eventsViewModel.onMarkClicked()
            }
            revert.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun textChanged() {
        with(binding) {
            description.doOnTextChanged { text, _, _, _ ->
                eventsViewModel.updateDescription(text.toString())
            }
            titleTask.doOnTextChanged { text, _, _, _ ->
                eventsViewModel.updateTitleText(text.toString())
            }
        }
    }

    private fun navigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsViewModel.navigationItem.collect { item ->
                    when (item) {
                        is NavigationEvent.Details -> Unit
                        is NavigationEvent.Back -> findNavController().popBackStack()
                    }
                }
            }
        }
    }
}
