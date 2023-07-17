package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.repository.models.branch_model.BranchModel
import com.hasan.jetfasthub.screens.main.repository.models.commits_model.CommitsModel
import com.hasan.jetfasthub.screens.main.repository.models.file_models.FilesModel
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

    suspend fun getReadmeAsHtml(token: String, url: String): Response<String>

    suspend fun getContentFiles(
        token: String,
        owner: String,
        repo: String,
        path: String,
        ref: String
    ): Response<FilesModel>

    suspend fun getBranches(
        token: String,
        owner: String,
        repo: String
    ): Response<BranchModel>

    suspend fun getCommits(
        token: String,
        owner: String,
        repo: String,
        branch: String,
        path: String,
        page: Int
    ): Response<CommitsModel>

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

    override suspend fun getReadmeAsHtml(token: String, url: String): Response<String> {
        return RetrofitInstance(context).gitHubService.getReadmeAsHtml(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            url = url
        )

    }

    override suspend fun getContentFiles(
        token: String,
        owner: String,
        repo: String,
        path: String,
        ref: String
    ): Response<FilesModel> {
        return RetrofitInstance(context).gitHubService.getContentFiles(
            token = token,
            owner = owner,
            repo = repo,
            path = path,
            ref = ref
        )
    }

    override suspend fun getBranches(
        token: String,
        owner: String,
        repo: String
    ): Response<BranchModel> {
        return RetrofitInstance(context).gitHubService.getBranches(
            token = token,
            owner = owner,
            repo = repo,
        )
    }

    override suspend fun getCommits(
        token: String,
        owner: String,
        repo: String,
        branch: String,
        path: String,
        page: Int
    ): Response<CommitsModel> {
        return RetrofitInstance(context).gitHubService.getCommits(
            token = "Bearer $PERSONAL_ACCESS_TOKEN",
            owner = owner,
            repo = repo,
            branch = branch,
            path = path,
            page = page
        )
    }

}