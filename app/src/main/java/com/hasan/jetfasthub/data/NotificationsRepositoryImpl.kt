package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.notifications.model.Notification
import com.hasan.jetfasthub.utility.Constants.PERSONAL_ACCESS_TOKEN
import retrofit2.Response

interface NotificationRepository {
    suspend fun getAllNotifications(token: String): Response<Notification>
}

class NotificationsRepositoryImpl(private val context: Context) : NotificationRepository {

    override suspend fun getAllNotifications(token: String): Response<Notification> {
        return RetrofitInstance(context = context).gitHubService.getAllNotifications(
            authToken = PERSONAL_ACCESS_TOKEN,
            perPage = 50,
            all = true
        )
    }
}