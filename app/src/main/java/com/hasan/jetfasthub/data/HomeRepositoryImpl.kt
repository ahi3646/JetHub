package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.home.events.models.Events
import com.hasan.jetfasthub.screens.main.home.events.received_model.ReceivedEvents
import com.hasan.jetfasthub.screens.main.home.user.GitHubUser
import retrofit2.Response

interface HomeRepository {

    suspend fun getUser(token: String, username: String): Response<GitHubUser>

    suspend fun getReceivedUserEvents(
        token: String,
        username: String,
    ): Response<ReceivedEvents>

}

class HomeRepositoryImpl(private val context: Context) : HomeRepository {

    override suspend fun getUser(token: String, username: String): Response<GitHubUser> {
        return RetrofitInstance(context = context).gitHubService.getUser(
            authToken = token,
            username = username
        )
    }

    override suspend fun getReceivedUserEvents(
        token: String, username: String
    ): Response<ReceivedEvents> {
        return RetrofitInstance(context).gitHubService.getReceivedUserEvents(
            authToken = token,
            username = username,
        )
    }

}