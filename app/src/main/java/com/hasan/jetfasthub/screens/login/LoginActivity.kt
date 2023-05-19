package com.hasan.jetfasthub.screens.login

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
import com.hasan.jetfasthub.navigation.JetHubNavHost
import com.hasan.jetfasthub.networking.GitHubHelper
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("ahi3646", "onCreate: ")

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
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                onHandleAuthIntent(intent = intent)
            }
        }
        //onHandleAuthIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("ahi3646", "onNewIntent: ")
        onHandleAuthIntent(intent)
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