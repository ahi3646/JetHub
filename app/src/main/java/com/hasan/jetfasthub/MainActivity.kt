package com.hasan.jetfasthub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.hasan.jetfasthub.navigation.JetHubNavHost
import com.hasan.jetfasthub.networking.GitHubHelper
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.networking.model.AccessTokenModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

const val TAG = "ahi3646"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = isSystemInDarkTheme()

            JetFastHubTheme(darkTheme = darkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    JetHubNavHost(darkTheme = darkTheme, this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onHandleAuthIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onHandleAuthIntent(intent)
    }

    private fun onHandleAuthIntent(intent: Intent?) {
        Log.d(TAG, "onHandleAuthIntent: ")
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(GitHubHelper.REDIRECT_URL)) {
                val tokenCode = uri!!.getQueryParameter("code")
                if (tokenCode!!.isNotBlank()) {
                    RetrofitInstance(this).api.getAccessToken(
                        tokenCode,
                        GitHubHelper.CLIENT_ID,
                        GitHubHelper.CLIENT_SECRET,
                        GitHubHelper.STATE,
                        GitHubHelper.REDIRECT_URL
                    ).enqueue(object : Callback<AccessTokenModel> {
                        override fun onResponse(
                            call: Call<AccessTokenModel>, response: Response<AccessTokenModel>
                        ) {
                            Log.d(
                                "ahi3646", "onResponse: ${
                                    response.body().toString()
                                }"
                            )
                        }

                        override fun onFailure(
                            call: Call<AccessTokenModel>, t: Throwable
                        ) {
                            Log.d(
                                "ahi3646", "onFailure: ${
                                    t.stackTrace
                                } "
                            )
                        }

                    })
                } else {
                    Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}