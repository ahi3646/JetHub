package com.hasan.jetfasthub.screens.login.domain

import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModelDto
import com.hasan.jetfasthub.screens.main.home.data.remote.authenticated_user_model.AuthenticatedUser
import retrofit2.Response

interface AuthRepository {
    suspend fun getAccessToken(code: String): Response<AccessTokenModelDto>
    suspend fun getAuthenticatedUser(token: String): Response<AuthenticatedUser>
    fun saveToken(token:String)
    fun saveAuthenticatedUser(username: String)
}