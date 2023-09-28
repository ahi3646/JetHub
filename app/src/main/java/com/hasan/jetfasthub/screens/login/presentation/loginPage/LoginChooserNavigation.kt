package com.hasan.jetfasthub.screens.login.presentation.loginPage

sealed class LoginChooserNavigation {
    data object BasicAuth: LoginChooserNavigation()

}