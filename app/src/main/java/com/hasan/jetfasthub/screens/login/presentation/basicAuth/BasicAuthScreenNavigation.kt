package com.hasan.jetfasthub.screens.login.presentation.basicAuth

import android.net.Uri

sealed interface BasicAuthScreenNavigation {
    data object GoPreviousScreen: BasicAuthScreenNavigation
    data class BrowserAuth(val uri: Uri): BasicAuthScreenNavigation
}