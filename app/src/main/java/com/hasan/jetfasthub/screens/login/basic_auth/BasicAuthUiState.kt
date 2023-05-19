package com.hasan.jetfasthub.screens.login.basic_auth

data class BasicAuthUiState(
    val userName: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false
)