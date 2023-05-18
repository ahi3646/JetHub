package com.hasan.jetfasthub.networking

import com.hasan.jetfasthub.networking.model.AuthModel
import com.hasan.jetfasthub.networking.model.LoginRequest
import com.hasan.jetfasthub.networking.model.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("authorizations")
    suspend fun login(
        @Header("Authorization") authHeader:String,
        @Body authModel: AuthModel
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST
    @Headers("Access: application/json")
    suspend fun getAccessToken(){

    }

}

const val BASE_URL = "https://api.github.com/"