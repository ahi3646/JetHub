package com.hasan.jetfasthub.screens.main

import androidx.annotation.StringRes
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.navigation.Screen

sealed class AppScreens(val route: String, @StringRes val resourceId: Int) {
    object Feeds: AppScreens("feeds", R.string.feeds)
    object Issues: AppScreens("issues", R.string.issues)
    object PullRequests: AppScreens("pull_requests", R.string.pull_requests)
}