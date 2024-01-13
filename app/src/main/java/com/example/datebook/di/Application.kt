package com.example.datebook.di

import android.app.Application
import com.example.datebook.data.EventDTOMapper
import com.example.datebook.data.repository.EventMockRepository
import com.example.datebook.data.repository.EventRepository
import com.example.datebook.data.EventsApi
import com.example.datebook.data.repository.LocalRepository
import com.example.datebook.data.repository.LocalRepositoryImplementation
import com.example.datebook.data.room.EventsDataBase
import com.example.datebook.domain.EventInteractor
import com.example.datebook.domain.EventMockInteractor
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
            single<LocalRepository> { LocalRepositoryImplementation(get()) }
            single<EventInteractor> { EventMockInteractor(get(), get()) }
            single { EventsDataBase.getDataBase(applicationContext).eventsDao() }
            single { EventDTOMapper() }
            single { EventMapper() }
            single { EventsApi(applicationContext) }
            viewModel { EventsViewModel(get(), get()) }
        }
        startKoin {
            modules(appModule)
        }
    }
}
