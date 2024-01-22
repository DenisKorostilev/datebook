package com.example.datebook.data

import android.content.Context
import com.google.gson.Gson
import retrofit2.Response

class EventsApi(private val context: Context) {
    fun getEventsResults(): Response<List<EventDTO>> {
        val string = context.assets.open(("local/events.json")).bufferedReader().use { it.readText() }
        val data = Gson().fromJson(string, EventsDTO::class.java).events
        return Response.success(data)
    }
}
