package com.hasan.jetfasthub.screens.main.home.data

import com.hasan.jetfasthub.screens.main.home.data.models.received_events_model_dto.ReceivedEventModelDto
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @Headers("Accept: application/vnd.github+json")
    @GET("users/{username}/received_events")
    suspend fun getReceivedUserEvents(
        @Header("Authorization") authToken: String,
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<ReceivedEventModelDto>

    @Headers("Accept: application/vnd.github+json")
    @GET("users/{username}")
    suspend fun getUser(
        @Header("Authorization") authToken: String,
        @Path("username") username: String
    ): GitHubUser

    @Headers("Accept: application/vnd.github+json")
    @GET("search/issues")
    suspend fun getIssuesWithCount(
        @Header("Authorization") authToken: String,
        @Query(value = "q", encoded = true) query: String,
        @Query("page") page: Int
    ): IssuesModel

    @Headers("Accept: application/vnd.github+json")
    @GET("search/issues")
    suspend fun getPullsWithCount(
        @Header("Authorization") authToken: String,
        @Query(value = "q", encoded = true) query: String,
        @Query("page") page: Int
    ): IssuesModel

}