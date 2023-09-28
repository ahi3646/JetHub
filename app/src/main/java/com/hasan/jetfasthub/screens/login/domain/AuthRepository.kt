package com.hasan.jetfasthub.screens.login.domain

import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModel
import com.hasan.jetfasthub.screens.main.home.data.remote.authenticated_user_model.AuthenticatedUser
import retrofit2.Response

interface AuthRepository {
    suspend fun getAccessToken(code: String): Response<AccessTokenModel>
    suspend fun getAuthenticatedUser(token: String): Response<AuthenticatedUser>
}