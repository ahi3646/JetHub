package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModel
import com.hasan.jetfasthub.utility.Constants.PERSONAL_ACCESS_TOKEN
import retrofit2.Response

interface ProfileRepository {
    suspend fun getUser(token: String, username: String): Response<GitHubUser>

    suspend fun getUserOrgs(token: String, username: String): Response<OrgModel>
}

class ProfileRepositoryImpl(private val context: Context) : ProfileRepository {
    override suspend fun getUser(token: String, username: String): Response<GitHubUser> {
        return RetrofitInstance(context = context).gitHubService.getUser(
//            authToken = token,
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            username = username
        )
    }

    override suspend fun getUserOrgs(token: String, username: String): Response<OrgModel> {
        return RetrofitInstance(context = context).gitHubService.getUserOrgs(
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            username = username
        )
    }



}