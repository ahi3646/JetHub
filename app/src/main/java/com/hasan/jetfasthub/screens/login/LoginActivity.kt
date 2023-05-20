package com.hasan.jetfasthub.screens.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.networking.GitHubHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val viewModel: LoginViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                onHandleAuthIntent(intent = intent)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("ahi3646", "onNewIntent: ")
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                onHandleAuthIntent(intent = intent)
            }
        }
    }

    private fun onHandleAuthIntent(intent: Intent?) {
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(GitHubHelper.REDIRECT_URL)) {
                val tokenCode = uri!!.getQueryParameter("code")
                Log.d("ahi3646", "onHandleAuthIntent: $tokenCode")
                if (tokenCode!!.isNotBlank()) {
                    viewModel.getAccessToken(tokenCode)
                    Log.d(
                        "ahi3646",
                        "onHandleAuthIntent: ${viewModel.getAccessToken(tokenCode).value}"
                    )
                } else {
                    Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}