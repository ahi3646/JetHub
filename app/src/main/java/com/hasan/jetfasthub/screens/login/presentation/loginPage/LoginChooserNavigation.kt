package com.hasan.jetfasthub.screens.login.presentation.loginPage

import android.net.Uri

sealed interface LoginChooserNavigation {
    data object BasicAuth: LoginChooserNavigation
    data class OAuth(val uri: Uri): LoginChooserNavigation
    data object NavigateToMain: LoginChooserNavigation
}