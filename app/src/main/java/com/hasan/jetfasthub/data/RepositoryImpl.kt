package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.repository.models.releases_model.ReleasesModel
import com.hasan.jetfasthub.screens.main.repository.models.repo_contributor_model.Contributors
import com.hasan.jetfasthub.screens.main.repository.models.repo_model.RepoModel
import com.hasan.jetfasthub.utility.Constants.PERSONAL_ACCESS_TOKEN
import retrofit2.Response

interface Repository {

    suspend fun getRepo(token: String, owner: String, repo: String): Response<RepoModel>

    suspend fun getContributors(
        token: String,
        owner: String,
        repo: String,
        page: Int
    ): Response<Contributors>

    suspend fun getReleases(
        token: String,
        owner: String,
        repo: String,
        page: Int
    ): Response<ReleasesModel>

}

class RepositoryImpl(private val context: Context) : Repository {

    override suspend fun getRepo(token: String, owner: String, repo: String): Response<RepoModel> {
        return RetrofitInstance(context).gitHubService.getRepo(
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            owner = owner,
            repo = repo
        )
    }

    override suspend fun getContributors(
        token: String,
        owner: String,
        repo: String,
        page: Int
    ): Response<Contributors> {
        return RetrofitInstance(context).gitHubService.getContributors(
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            owner = owner,
            repo = repo,
            page = page
        )
    }

    override suspend fun getReleases(
        token: String,
        owner: String,
        repo: String,
        page: Int
    ): Response<ReleasesModel> {
        return RetrofitInstance(context).gitHubService.getReleases(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            owner = owner,
            repo = repo,
            page = page
        )
    }

}