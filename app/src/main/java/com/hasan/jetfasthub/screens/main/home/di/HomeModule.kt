package com.hasan.jetfasthub.screens.main.home.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.home.data.HomeRepositoryImpl
import com.hasan.jetfasthub.screens.main.home.data.database.HomeDatabase
import com.hasan.jetfasthub.screens.main.home.data.database.ReceivedEventsModelEntity
import com.hasan.jetfasthub.screens.main.home.data.models.EventsRemoteMediator
import com.hasan.jetfasthub.screens.main.home.domain.HomeRepository
import com.hasan.jetfasthub.screens.main.home.domain.HomeUseCase
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//TODO fix preference helper
@OptIn(ExperimentalPagingApi::class)
val homeModule = module {
    single { PreferenceHelper(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    single {
        Room.databaseBuilder(
            androidApplication(),
            HomeDatabase::class.java,
            "events.db"
        ).build()
    }
//    single { PreferenceHelper.getToken(androidApplication()) }
//    single { PreferenceHelper.getAuthenticatedUsername(androidApplication()) }
    single { EventsRemoteMediator(get(), get(), get() ) }

    fun providesEventPager(
        eventsDb: HomeDatabase,
        eventApi: HomeRepository,
        preferences: PreferenceHelper
    ): Pager<Int, ReceivedEventsModelEntity>{
        return  Pager(
            config = PagingConfig(pageSize = 30),
            remoteMediator = EventsRemoteMediator(
                repository = eventApi,
                homeDatabase = eventsDb,
                preferences = preferences
            ),
            pagingSourceFactory = {
                eventsDb.dao.pagingSource()
            }
        )
    }
    single { HomeUseCase(get()) }
    viewModel { HomeScreenViewModel(get(), providesEventPager(get(), get(), get())) }
}