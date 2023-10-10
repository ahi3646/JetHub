package com.hasan.jetfasthub.screens.login.data.mapper

import com.hasan.jetfasthub.screens.login.data.entity.AccessTokenModelDto
import com.hasan.jetfasthub.screens.login.domain.model.AccessTokenModel

fun AccessTokenModelDto.mapToAccessToken(): AccessTokenModel{
    return AccessTokenModel(
        accessToken = this.accessToken.orEmpty()
    )
}