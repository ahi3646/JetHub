package com.hasan.jetfasthub

import android.content.Intent
import android.os.Bundle
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
import com.hasan.jetfasthub.screens.login.LoginViewModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme

const val TAG = "ahi3646"

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = LoginViewModel(this)

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
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(GitHubHelper.REDIRECT_URL)) {
                val tokenCode = uri!!.getQueryParameter("code")
                if (tokenCode!!.isNotBlank()) {
                    viewModel.getAccessToken(tokenCode)
                } else {
                    Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}