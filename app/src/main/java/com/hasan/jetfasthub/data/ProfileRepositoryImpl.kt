package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.home.user.GitHubUser
import com.hasan.jetfasthub.screens.main.profile.model.User
import retrofit2.Response

interface ProfileRepository {
    suspend fun getUser(token: String, username: String): Response<GitHubUser>
}

class ProfileRepositoryImpl(private val context: Context) : ProfileRepository {
    override suspend fun getUser(token: String, username: String): Response<GitHubUser> {
        return RetrofitInstance(context = context).gitHubService.getUser(
            authToken = "Bearer $token", username = username
        )
    }
}