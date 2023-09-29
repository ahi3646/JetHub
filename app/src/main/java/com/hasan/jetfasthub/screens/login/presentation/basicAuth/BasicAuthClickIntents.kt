package com.hasan.jetfasthub.screens.login.presentation.basicAuth

sealed interface BasicAuthClickIntents {
    data class OnUsernameChanged(val username: String): BasicAuthClickIntents
    data class OnPasswordChanged(val password: String): BasicAuthClickIntents
    data object BrowserAuth: BasicAuthClickIntents
    data object GoPreviousScreen: BasicAuthClickIntents
    //this feature currently unavailable
    data object LoginClick: BasicAuthClickIntents
}

