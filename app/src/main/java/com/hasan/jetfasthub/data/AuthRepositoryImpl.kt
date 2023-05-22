package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.data.model.AccessTokenModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response


interface AuthRepository {
    suspend fun getAccessToken(code: String): Response<AccessTokenModel>
}

class AuthRepositoryImpl(private val context: Context) : AuthRepository {

    //private var _accessTokenModel = MutableLiveData<Response<AccessTokenModel>>()

    override suspend fun getAccessToken(code: String): Response<AccessTokenModel> {
            return RetrofitInstance(context).api.getAccessToken(
                code = code,
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET,
//                GitHubHelper.STATE,
//                GitHubHelper.REDIRECT_URL
            )

//        RetrofitInstance(context).api.getAccessToken(
//            code = code,
//            GitHubHelper.CLIENT_ID,
//            GitHubHelper.CLIENT_SECRET,
//            GitHubHelper.STATE,
//            GitHubHelper.REDIRECT_URL
//        ).enqueue(object : Callback<AccessTokenModel> {
//            override fun onResponse(
//                call: Call<AccessTokenModel>,
//                response: Response<AccessTokenModel>
//            ) {
//
//                if (response.isSuccessful) {
//                    try {
//                        val jsonObject = JSONObject(Gson().toJson(response.body()))
//                        val accessToken = jsonObject.getString("access_token")
////                        status = jsonObject.getBoolean("status")
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
////                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
//                    Log.e("ahi3646", "impl - " +  response.body().toString())
//                } else {
//                    Log.d("ahi3646", "Some error occurred...")
//                }
//
//                Log.d("ahi3646", "onResponse: ${response.body()} ")
//                _accessTokenModel.value = response.body()
//            }
//
//            override fun onFailure(call: Call<AccessTokenModel>, t: Throwable) {
//                //to do
//
//                Log.d("ahi3646", "onFailure: ${t.message}")
//            }
//
//        })
//
//        return _accessTokenModel.value!!
    }

}