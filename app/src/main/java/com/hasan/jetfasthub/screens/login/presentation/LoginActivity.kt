package com.hasan.jetfasthub.screens.login.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.hasan.jetfasthub.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val viewModel: LoginViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                viewModel.handleAuthIntent(intent = intent)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                viewModel.handleAuthIntent(intent = intent)
            }
        }
    }
}