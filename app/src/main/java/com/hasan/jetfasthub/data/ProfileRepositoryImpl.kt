package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.profile.model.event_model.UserEvents
import com.hasan.jetfasthub.screens.main.profile.model.following_model.FollowingModel
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModel
import com.hasan.jetfasthub.screens.main.profile.model.repo_model.UserRepositoryModel
import com.hasan.jetfasthub.screens.main.profile.model.starred_repo_model.StarredRepoModel
import com.hasan.jetfasthub.utility.Constants.PERSONAL_ACCESS_TOKEN
import retrofit2.Response

interface ProfileRepository {
    suspend fun getUser(token: String, username: String): Response<GitHubUser>

    suspend fun getUserOrgs(token: String, username: String): Response<OrgModel>

    suspend fun getUserEvents(token: String, username: String): Response<UserEvents>

    suspend fun getUserRepository(token: String, username: String): Response<UserRepositoryModel>
    suspend fun getUserStarredRepos(token: String, username: String, page: Int): Response<StarredRepoModel>
    suspend fun getUserFollowings(token: String, username: String, page: Int): Response<FollowingModel>
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

    override suspend fun getUserEvents(token: String, username: String): Response<UserEvents> {
        return  RetrofitInstance(context).gitHubService.getUserEvents(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            username = username
        )
    }

    override suspend fun getUserRepository(
        token: String,
        username: String
    ): Response<UserRepositoryModel> {
        return RetrofitInstance(context).gitHubService.getUserRepos(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            username = username
        )
    }

    override suspend fun getUserStarredRepos(
        token: String,
        username: String,
        page: Int
    ): Response<StarredRepoModel> {
        return RetrofitInstance(context).gitHubService.getUserStarredRepos(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            username = username,
            page = page
        )
    }

    override suspend fun getUserFollowings(
        token: String,
        username: String,
        page: Int
    ): Response<FollowingModel> {
        return RetrofitInstance(context).gitHubService.getUserFollowings(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            username = username,
            page = page
        )
    }

}