package com.hasan.jetfasthub.screens.login.presentation.loginPage

sealed interface LoginChooserClickIntents {
    data object BasicAuthentication: LoginChooserClickIntents
    data object OAuthCLick: LoginChooserClickIntents
}