package com.hasan.jetfasthub.screens.login.ui

sealed class LoginChooserNavigation {
    data object BasicAuth: LoginChooserNavigation()

}