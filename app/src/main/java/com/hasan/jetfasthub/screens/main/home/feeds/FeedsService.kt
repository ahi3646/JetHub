package com.hasan.jetfasthub.screens.main.home.feeds

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedsService {

    @GET("users/{username}/events")
    fun getUserEvents(
        @Path("username") username: String,
        @Query("page") page: Int
    ) //: Call<Event>

}