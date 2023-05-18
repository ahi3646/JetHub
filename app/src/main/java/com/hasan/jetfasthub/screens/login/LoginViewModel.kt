package com.hasan.jetfasthub.screens.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasan.jetfasthub.data.AuthRepository
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.networking.model.AccessTokenModel

class LoginViewModel(context: Context) : ViewModel() {

    private var repository: AuthRepository

    private var _accessTokenModel = MutableLiveData<AccessTokenModel>()
    private val accessTokenModel: LiveData<AccessTokenModel> = _accessTokenModel

    init {
        repository = AuthRepository(RetrofitInstance(context))
    }

    fun getAccessToken(code: String) {
        _accessTokenModel.value = repository.getAccessToken(code).value
    }

}