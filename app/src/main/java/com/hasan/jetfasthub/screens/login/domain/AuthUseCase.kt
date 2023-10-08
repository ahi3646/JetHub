package com.hasan.jetfasthub.screens.login.domain

import com.hasan.jetfasthub.screens.login.domain.model.AccessTokenModel
import com.hasan.jetfasthub.screens.main.home.data.models.authenticated_user_model.AuthenticatedUser
import retrofit2.Response

class AuthUseCase(private val authRepository: AuthRepository) {
    suspend fun getAccessToken(code: String): AccessTokenModel {
        return authRepository.getAccessToken(code)
    }

    suspend fun getAuthenticatedUser(token: String): Response<AuthenticatedUser> {
        return authRepository.getAuthenticatedUser(token)
    }

    fun saveToken(token: String) {
        authRepository.saveToken(token)
    }

    fun saveAuthenticatedUser(username: String) {
        authRepository.saveAuthenticatedUser(username)
    }

    fun isLogged(): Boolean = authRepository.isLogged()
}