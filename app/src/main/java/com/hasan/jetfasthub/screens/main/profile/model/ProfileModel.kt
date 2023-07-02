package com.hasan.jetfasthub.screens.main.profile.model

import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModel

data class ProfileModel(
    val user :GitHubUser? = null,
    val organisation: OrgModel? = null
)