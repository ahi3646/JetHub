package com.hasan.jetfasthub.screens.main

sealed interface AppScreens {
    object Feeds: AppScreens
    object Issues: AppScreens
    object PullRequests: AppScreens
}