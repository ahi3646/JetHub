package com.hasan.jetfasthub.screens.login.domain

import com.hasan.jetfasthub.screens.login.domain.model.AccessTokenModel
import com.hasan.jetfasthub.screens.main.home.data.models.authenticated_user_model.AuthenticatedUser
import retrofit2.Response

interface AuthRepository {
    suspend fun getAccessToken(code: String): AccessTokenModel
    suspend fun getAuthenticatedUser(token: String): Response<AuthenticatedUser>
    fun saveToken(token:String)
    fun saveAuthenticatedUser(username: String)
    fun isLogged(): Boolean
}