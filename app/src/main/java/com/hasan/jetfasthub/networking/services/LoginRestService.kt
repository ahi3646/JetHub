package com.hasan.jetfasthub.networking.services

import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModel
import com.hasan.jetfasthub.screens.login.data.entity.AuthModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRestService {

    @POST("authorizations")
    fun login(
        @Body authModel: AuthModel
    ): Call<AccessTokenModel>

}