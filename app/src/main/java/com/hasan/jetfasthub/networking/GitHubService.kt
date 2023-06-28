package com.hasan.jetfasthub.networking

import com.hasan.jetfasthub.screens.login.model.AccessTokenModel
import com.hasan.jetfasthub.screens.login.model.AuthModel
import com.hasan.jetfasthub.screens.main.home.received_model.ReceivedEvents
import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.notifications.model.Notification
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubService {


    @POST("authorizations")
    fun login(
        @Body authModel: AuthModel
    ): Call<AccessTokenModel>


    @FormUrlEncoded
    @POST("${Constants.BASIC_AUTH_URL}login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ) : Response<AccessTokenModel>


    @Headers("Accept: application/vnd.github+json")
    @GET("users/{username}")
    suspend fun getUser(
        @Header("Authorization") authToken: String,
        @Path("username") username: String
    ): Response<GitHubUser>


    @Headers("Accept: application/vnd.github+json")
    @GET("users/{username}/received_events")
    suspend fun getReceivedUserEvents(
        @Header("Authorization") authToken: String,
        @Path("username") username: String,
    ): Response<ReceivedEvents>

    @Headers("Accept: application/vnd.github+json")
    @GET("notifications?all=true&per_page=200")
    suspend fun getAllNotifications(
        @Header("Authorization") authToken: String,
    ): Response<Notification>

}