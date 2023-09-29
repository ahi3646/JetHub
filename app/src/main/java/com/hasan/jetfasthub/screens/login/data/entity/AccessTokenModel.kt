package com.hasan.jetfasthub.screens.login.data.entity

import com.google.gson.annotations.SerializedName

data class AccessTokenModel(
    @SerializedName("access_token")
    val access_token: String ? ,
)
