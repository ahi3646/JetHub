package com.hasan.jetfasthub.screens.login.data

import android.content.Context
import com.hasan.jetfasthub.networking.RestClient
import com.hasan.jetfasthub.screens.main.home.data.remote.authenticated_user_model.AuthenticatedUser
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModelDto
import com.hasan.jetfasthub.screens.login.domain.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl(private val context: Context, private val preferences: PreferenceHelper) : AuthRepository {

    override suspend fun getAccessToken(code: String): Response<AccessTokenModelDto> {
        return RestClient(context).authorizationService.getAccessToken(
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET,
            code = code,
        )
    }

    override suspend fun getAuthenticatedUser(token: String): Response<AuthenticatedUser> {
        return RestClient(context = context).authorizationService.getAuthenticatedUser(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}"
        )
    }

    override fun saveToken(token: String) {
        preferences.saveToken(token)
    }

    override fun saveAuthenticatedUser(username: String) {
        preferences.saveAuthenticatedUser(username)
    }

}