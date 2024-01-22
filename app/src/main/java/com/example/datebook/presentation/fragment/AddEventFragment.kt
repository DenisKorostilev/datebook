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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.datebook.R
import com.example.datebook.databinding.AddEventFragmentBinding
import com.example.datebook.presentation.EventUI
import com.example.datebook.presentation.launchRepeatedly
import com.example.datebook.presentation.viewmodel.EventsViewModel
import com.example.datebook.presentation.viewmodel.NavigationEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEventFragment : Fragment(R.layout.add_event_fragment) {
    private val binding: AddEventFragmentBinding by viewBinding()
    private val eventsViewModel: EventsViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateEditor()
        timeEditor()
        bindViews()
        currentNavigation()
    }

    private fun dateEditor() {
        with(binding) {
            val calendar: Calendar = Calendar.getInstance()
            addDate.setText(SimpleDateFormat("dd MMMM yyyy г.", Locale("ru")).format(System.currentTimeMillis()))
            val onDateClicked =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    addDate.setText(SimpleDateFormat("dd MMMM yyyy г.", Locale("ru")).format(calendar.time))
                }

            addDate.setOnClickListener {
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

    private fun timeEditor() {
        with(binding) {
            val calendar: Calendar = Calendar.getInstance()
            val formattedTime = SimpleDateFormat("HH:mm", Locale("ru")).format(calendar.time)
            val endTime = Calendar.getInstance()
            val updateFieldsTime: (Calendar, Calendar) -> Unit = { startCalendar, endCalendar ->
                addFirstTime.setText(startCalendar.formatTime("HH:mm"))
                addLastTime.setText(endCalendar.formatTime("HH:mm"))
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
                    addLastTime.setText(endTime.formatTime("HH:mm"))
                    Toast.makeText(context, R.string.notificationFirst, Toast.LENGTH_SHORT).show()
                } else {
                    addLastTime.setText(newEndTime.formatTime("HH:mm"))
                }
            }
            addFirstTime.setText(formattedTime)
            addLastTime.setText(formattedTime)
            addFirstTime.setOnClickListener {
                TimePickerDialog(
                    context,
                    AlertDialog.THEME_HOLO_LIGHT,
                    onTimeStartClicked,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }
            addLastTime.setOnClickListener {
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

    private fun bindViews() {
        with(binding) {
            val event = EventUI(
                id = "",
                timeStart = addFirstTime.text.toString(),
                timeFinish = addLastTime.text.toString(),
                date = addDate.text.toString(),
                name = addEventName.text.toString(),
                description = addEventDescription.text.toString()
            )

            addFirstTime.doOnTextChanged { timeStart, _, _, _ ->
                event.timeStart = timeStart.toString()
            }

            addLastTime.doOnTextChanged { timeFinish, _, _, _ ->
                event.timeFinish = timeFinish.toString()
            }

            addDate.doOnTextChanged { date, _, _, _ ->
                event.date = date.toString()
            }

            addEventName.doOnTextChanged { titleText, _, _, _ ->
                event.name = titleText.toString()
            }

            addEventDescription.doOnTextChanged { descriptionText, _, _, _ ->
                event.description = descriptionText.toString()
            }

            btnAddEvent.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                event.id = calendar.timeInMillis.toString()
                if (checkingForFullnessEventUI(event)) {
                    eventsViewModel.addEvent(event)
                } else {
                    Toast.makeText(context, R.string.notificationThird, Toast.LENGTH_SHORT).show()
                }
            }

            btnBackStack.setOnClickListener {
                eventsViewModel.backStack()
            }
        }
    }

    private fun currentNavigation() {
        viewLifecycleOwner.launchRepeatedly {
            eventsViewModel.navigationItem.onEach { item ->
                when (item) {
                    is NavigationEvent.Details -> Unit
                    is NavigationEvent.Back -> findNavController().popBackStack()
                }
            }.launchIn(this)
        }
    }

    private fun checkingForFullnessEventUI(eventUI: EventUI): Boolean {
        return eventUI.timeStart.isNotBlank() &&
            eventUI.timeFinish.isNotBlank() &&
            eventUI.name.isNotBlank() &&
            eventUI.description.isNotBlank() &&
            eventUI.date.isNotBlank()
    }
}
