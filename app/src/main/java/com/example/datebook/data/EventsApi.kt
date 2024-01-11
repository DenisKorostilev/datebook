package com.example.datebook.data

import android.content.Context
import com.google.gson.Gson
import retrofit2.Response

class EventsApi(private val context: Context) {
    fun getEventsResults(): Response<List<EventDTO>> {
        val string = readJsonFromAssets("local/events.json")
        val data = Gson().fromJson(string, EventsDTO::class.java).events
        return Response.success(data)
    }

    private fun readJsonFromAssets(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }
}
