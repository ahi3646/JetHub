package com.hasan.jetfasthub.networking

import com.hasan.jetfasthub.networking.model.AccessTokenModel
import com.hasan.jetfasthub.networking.model.AuthModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("authorizations")
    fun login(
        @Body authModel: AuthModel
    ): Call<AccessTokenModel>

    @FormUrlEncoded
    @POST("access_token")
    @Headers("Access: application/json")
    fun getAccessToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("state") state: String,
        @Field("redirect_uri") redirectUrl: String
    ) : Call<AccessTokenModel>


}

const val BASE_URL = "https://github.com/login/oauth/"