package com.example.datebook.presentation.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.EventsDetailFragmentBinding
import com.example.datebook.presentation.launchRepeatedly
import com.example.datebook.presentation.viewmodel.EventsViewModel
import com.example.datebook.presentation.viewmodel.NavigationEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventsDetailFragment : Fragment(R.layout.events_detail_fragment) {
    private val binding: EventsDetailFragmentBinding by viewBinding()
    private val arguments: EventsDetailFragmentArgs by navArgs()
    private val eventsViewModel: EventsViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        onClick()
        updateEvent()
        timeDetailEditor()
        dateDetailEditor()
    }

    private fun initViews() {
        val eventUI = arguments.eventUI
        with(binding) {
            currentEventName.setText(eventUI.name)
            currentDescription.setText(eventUI.description)
        }
        detailEventNavigation()
    }

    private fun dateDetailEditor() {
        with(binding) {
            val calendar: Calendar = Calendar.getInstance()
            currentDate.text = arguments.eventUI.date
            val onDateClicked =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    currentDate.text = SimpleDateFormat("dd MMMM yyyy г.", Locale("ru")).format(calendar.time)
                }

            currentDate.setOnClickListener {
                context?.let {
                    DatePickerDialog(
                        it,
                        onDateClicked,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
    }

    private fun Calendar.formatTime(pattern: String): String =
        SimpleDateFormat(pattern, Locale("ru")).format(time)

    private fun timeDetailEditor() {
        with(binding) {
            val calendar: Calendar = Calendar.getInstance()
            detailsDateStart.text = arguments.eventUI.timeStart
            detailsDateFinish.text = arguments.eventUI.timeFinish
            val endTime = Calendar.getInstance()

            val updateFieldsTime: (Calendar, Calendar) -> Unit = { startCalendar, endCalendar ->
                detailsDateStart.text = startCalendar.formatTime("HH:mm")
                detailsDateFinish.text = endCalendar.formatTime("HH:mm")
            }

            val onTimeStartClicked = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                endTime.time = calendar.time
                endTime.add(Calendar.HOUR_OF_DAY, 1)
                endTime.set(Calendar.MINUTE, 0)
                updateFieldsTime(calendar, endTime)
            }

            val onTimeFinishClicked = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val newEndTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                if (newEndTime <= calendar) {
                    detailsDateFinish.text = endTime.formatTime("HH:mm")
                    Toast.makeText(context, R.string.notificationFirst, Toast.LENGTH_SHORT).show()
                } else {
                    detailsDateFinish.text = newEndTime.formatTime("HH:mm")
                }
            }

            detailsDateStart.setOnClickListener {
                TimePickerDialog(
                    context,
                    AlertDialog.THEME_HOLO_LIGHT,
                    onTimeStartClicked,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }

            detailsDateFinish.setOnClickListener {
                TimePickerDialog(
                    context,
                    AlertDialog.THEME_HOLO_LIGHT,
                    onTimeFinishClicked,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }
    }

    private fun onClick() {
        with(binding) {
            refactorEvent.setOnClickListener {
                eventsViewModel.onMarkClicked()
            }

            revert.setOnClickListener {
                eventsViewModel.backStack()
            }

            deleteEvent.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.confirm)
                builder.setMessage(R.string.notificationSecond)
                builder.setPositiveButton(R.string.yes) { _, _ ->
                    eventsViewModel.onBtnDeleteClick()
                }
                builder.setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
        }
    }

    private fun updateEvent() {
        with(binding) {
            currentDescription.doOnTextChanged { description, _, _, _ ->
                eventsViewModel.updateDetailDescription(description.toString())
            }

            currentEventName.doOnTextChanged { name, _, _, _ ->
                eventsViewModel.updateDetailTitleTask(name.toString())
            }

            detailsDateStart.doOnTextChanged { startTime, _, _, _ ->
                eventsViewModel.updateStartTime(startTime.toString())
            }

            detailsDateFinish.doOnTextChanged { endTime, _, _, _ ->
                eventsViewModel.updateEndTime(endTime.toString())
            }

            currentDate.doOnTextChanged { date, _, _, _ ->
                eventsViewModel.updateDate(date.toString())
            }
        }
    }

    private fun detailEventNavigation() {
        viewLifecycleOwner.launchRepeatedly {
            eventsViewModel.navigationItem.onEach { item ->
                when (item) {
                    is NavigationEvent.Details -> Unit
                    is NavigationEvent.Back -> findNavController().popBackStack()
                }
            }.launchIn(this)
        }
    }
}
