package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.gists.fork_response_model.GistForkResponse
import com.hasan.jetfasthub.screens.main.gists.gist_model.GistModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response


interface GistRepository {

    suspend fun getGist(token: String, gistId: String): Response<GistModel>

    suspend fun deleteGist(token: String, gistId: String): Response<Boolean>

    suspend fun checkIfGistStarred(token: String, gistId: String): Response<Boolean>

    suspend fun forkGist(token: String, gistId: String): Response<GistForkResponse>

    suspend fun starGist(token: String, gistId: String): Response<Boolean>

    suspend fun unstarGist(token: String, gistId: String): Response<Boolean>

}

class GistRepositoryImpl(private val context: Context): GistRepository {

    override suspend fun unstarGist(token: String, gistId: String): Response<Boolean> {
        return RetrofitInstance(context).gitHubService.unstarGist(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId
        )
    }

    override suspend fun starGist(token: String, gistId: String): Response<Boolean> {
        return RetrofitInstance(context).gitHubService.starGist(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId
        )
    }

    override suspend fun deleteGist(token: String, gistId: String): Response<Boolean> {
        return RetrofitInstance(context).gitHubService.deleteGist(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId
        )
    }

    override suspend fun getGist(token: String, gistId: String): Response<GistModel> {
        return RetrofitInstance(context).gitHubService.getGist(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId
        )
    }

    override suspend fun checkIfGistStarred(token: String, gistId: String): Response<Boolean> {
        return RetrofitInstance(context).gitHubService.checkIfGistStarred(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId
        )
    }

    override suspend fun forkGist(token: String, gistId: String): Response<GistForkResponse> {
        return RetrofitInstance(context).gitHubService.forkGist(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId
        )
    }

}