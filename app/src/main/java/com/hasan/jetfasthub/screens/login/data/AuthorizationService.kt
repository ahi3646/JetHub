package com.hasan.jetfasthub.screens.login.data

import com.hasan.jetfasthub.screens.main.home.data.models.authenticated_user_model.AuthenticatedUser
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModelDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationService {

    @FormUrlEncoded
    @POST("${Constants.BASIC_AUTH_URL}login/oauth/access_token")
    @Headers(
        "Accept: application/json",
    )
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        //other parameters are optional
    ): AccessTokenModelDto

    @Headers("Accept: application/vnd.github+json")
    @GET("user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") token: String,
    ): Response<AuthenticatedUser>

}