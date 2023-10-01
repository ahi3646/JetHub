package com.hasan.jetfasthub.networking.services

import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModelDto
import com.hasan.jetfasthub.screens.login.data.entity.AuthModelDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRestService {

    @POST("authorizations")
    fun login(
        @Body authModel: AuthModelDto
    ): Call<AccessTokenModelDto>

}