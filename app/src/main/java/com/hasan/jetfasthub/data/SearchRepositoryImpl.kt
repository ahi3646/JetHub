package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.search.models.repository_model.RepositoryModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response

interface SearchRepository {

    suspend fun searchRepositories(token: String, query: String, page: Long): Response<RepositoryModel>

}


class SearchRepositoryImpl(private val context: Context) : SearchRepository {

    override suspend fun searchRepositories(token: String, query: String, page: Long): Response<RepositoryModel> {
        return RetrofitInstance(context).gitHubService.searchRepositories(
            authToken = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            query = query,
            page = page
        )
    }

}