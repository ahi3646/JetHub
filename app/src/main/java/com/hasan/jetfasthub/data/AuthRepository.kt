package com.hasan.jetfasthub.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hasan.jetfasthub.networking.GitHubHelper
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.networking.model.AccessTokenModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private var retrofitService: RetrofitInstance) {

    private var _accessTokenModel = MutableLiveData<AccessTokenModel>()
    private val accessTokenModel: LiveData<AccessTokenModel> = _accessTokenModel

    fun getAccessToken(code: String): LiveData<AccessTokenModel> {
        retrofitService.api.getAccessToken(
            code = code,
            GitHubHelper.CLIENT_ID,
            GitHubHelper.CLIENT_SECRET,
            GitHubHelper.STATE,
            GitHubHelper.REDIRECT_URL
        ).enqueue(object : Callback<AccessTokenModel> {
            override fun onResponse(
                call: Call<AccessTokenModel>,
                response: Response<AccessTokenModel>
            ) {
                _accessTokenModel.value = response.body()
            }

            override fun onFailure(call: Call<AccessTokenModel>, t: Throwable) {
                //to do
            }

        })

        return accessTokenModel
    }

}