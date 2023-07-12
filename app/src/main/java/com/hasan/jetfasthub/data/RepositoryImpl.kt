package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.repository.models.repo_model.RepoModel
import com.hasan.jetfasthub.utility.Constants.PERSONAL_ACCESS_TOKEN
import retrofit2.Response

interface Repository{

    suspend fun getRepo(token: String, owner: String, repo: String): Response<RepoModel>

}

class RepositoryImpl(private val context: Context): Repository {

    override suspend fun getRepo(token: String, owner: String, repo: String): Response<RepoModel> {
        return RetrofitInstance(context).gitHubService.getRepo(
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            owner = owner,
            repo = repo
        )
    }

}