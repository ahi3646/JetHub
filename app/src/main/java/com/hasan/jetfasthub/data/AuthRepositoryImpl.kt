package com.hasan.jetfasthub.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hasan.jetfasthub.networking.GitHubHelper
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.networking.model.AccessTokenModel
import org.koin.androidx.compose.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface  AuthRepository{
    fun getAccessToken(code: String): LiveData<AccessTokenModel>
}
class AuthRepositoryImpl(private val context: Context): AuthRepository {

    private var _accessTokenModel = MutableLiveData<AccessTokenModel>()

    override fun getAccessToken(code: String): LiveData<AccessTokenModel> {
        RetrofitInstance(context).api.getAccessToken(
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
                Log.d("ahi3646", "onFailure: ${t.stackTrace}")
            }

        })

        return _accessTokenModel
    }

}