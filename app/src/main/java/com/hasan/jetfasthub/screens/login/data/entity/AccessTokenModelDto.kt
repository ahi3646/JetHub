package com.hasan.jetfasthub.screens.login.data.entity

import com.google.gson.annotations.SerializedName

data class AccessTokenModelDto(
    @SerializedName("access_token")
    val accessToken: String ?
)
