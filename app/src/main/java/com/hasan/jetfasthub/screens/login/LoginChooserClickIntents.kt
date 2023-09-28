package com.hasan.jetfasthub.screens.login

sealed interface LoginChooserClickIntents {
    data object BasicAuthentication: LoginChooserClickIntents
    data object OAuthCLick: LoginChooserClickIntents
}