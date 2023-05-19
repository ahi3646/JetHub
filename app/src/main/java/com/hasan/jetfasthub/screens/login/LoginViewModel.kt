package com.hasan.jetfasthub.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hasan.jetfasthub.data.AuthRepository
import com.hasan.jetfasthub.networking.model.AccessTokenModel

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    fun getAccessToken(code: String): LiveData<AccessTokenModel> {
        return repository.getAccessToken(code)
    }

}