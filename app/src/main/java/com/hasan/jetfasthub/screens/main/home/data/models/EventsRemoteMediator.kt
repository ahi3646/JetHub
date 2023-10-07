package com.hasan.jetfasthub.screens.main.home.data.models

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.home.data.database.HomeDatabase
import com.hasan.jetfasthub.screens.main.home.data.database.ReceivedEventsModelEntity
import com.hasan.jetfasthub.screens.main.home.data.mappers.toReceivedEventsModelEntity
import com.hasan.jetfasthub.screens.main.home.domain.HomeRepository
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator(
    private val repository: HomeRepository,
    private val homeDatabase: HomeDatabase,
    private val preferences: PreferenceHelper
) : RemoteMediator<Int, ReceivedEventsModelEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReceivedEventsModelEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {

                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val events = repository.getReceivedUserEvents(
                preferences.getAuthenticatedUsername(),
                loadKey,
                state.config.pageSize
            )

            homeDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    homeDatabase.dao.clearAll()
                }
                val eventEntities = events.map { it.toReceivedEventsModelEntity() }
                homeDatabase.dao.upsertAll(eventEntities)

            }
            MediatorResult.Success(
                endOfPaginationReached = events.isEmpty()
            )
        } catch (e: Exception) {
            Log.d("ahi3646", "load: mediator ")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
        //TODO fix error handling
    }

}