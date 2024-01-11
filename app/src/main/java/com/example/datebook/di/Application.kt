package com.example.datebook.di

import android.app.Application
import com.example.datebook.data.EventDTOMapper
import com.example.datebook.data.EventMockRepository
import com.example.datebook.data.EventRepository
import com.example.datebook.data.EventsApi
import com.example.datebook.presentation.EventMapper
import com.example.datebook.presentation.viewmodel.EventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        val appModule = module {
            single<EventRepository> { EventMockRepository(get(), get()) }
            single { EventDTOMapper() }
            single { EventMapper() }
            viewModel { EventsViewModel(get(), get()) }
            single { EventsApi(applicationContext) }
        }
        startKoin {
            modules(appModule)
        }
    }
}
