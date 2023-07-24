package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.CommitModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response


interface CommitRepository{

    suspend fun getCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ): Response<CommitModel>

}

class CommitRepositoryImpl(private val context: Context): CommitRepository {

    override suspend fun getCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ): Response<CommitModel> {
        return RetrofitInstance(context).gitHubService.getCommit(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            branch = branch,
        )
    }

}