package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RestClient
import com.hasan.jetfasthub.screens.main.home.data.remote.received_events_model_dto.ReceivedEventModelDto
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.screens.main.home.data.remote.user_model.GitHubUser
import com.hasan.jetfasthub.core.ui.utils.Constants.PERSONAL_ACCESS_TOKEN
import retrofit2.Response

interface HomeRepository {

    suspend fun getUser(token: String, username: String): Response<GitHubUser>

    suspend fun getReceivedUserEvents(
        token: String,
        username: String,
        page: Int,
        perPage: Int
    ):List<ReceivedEventModelDto>

    suspend fun getIssuesWithCount(
        token: String,
        query: String,
        page: Int
    ): Response<IssuesModel>

    suspend fun getPullsWithCount(
        token: String,
        query: String,
        page: Int
    ): Response<IssuesModel>

}

class HomeRepositoryImpl(private val context: Context) : HomeRepository {

    override suspend fun getPullsWithCount(
        token: String,
        query: String,
        page: Int
    ): Response<IssuesModel> {
        return RestClient(context).homeService.getPullsWithCount(
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            query = query,
            page = page,
        )
    }

    override suspend fun getIssuesWithCount(
        token: String,
        query: String,
        page: Int
    ): Response<IssuesModel> {
        return RestClient(context).homeService.getIssuesWithCount(
            authToken = "Bearer $PERSONAL_ACCESS_TOKEN",
            query = query,
            page = page,
        )
    }


//    override suspend fun getAuthenticatedUser(token: String): Response<AuthenticatedUser> {
//        return RestClient(context = context).homeService.getAuthenticatedUser(
//            token = "Bearer $PERSONAL_ACCESS_TOKEN"
//        )
//    }

    override suspend fun getUser(token: String, username: String): Response<GitHubUser> {
        return RestClient(context = context).homeService.getUser(
            authToken = PERSONAL_ACCESS_TOKEN,
            username = username
        )
    }

    override suspend fun getReceivedUserEvents(
        token: String, username: String, page: Int, perPage: Int
    ): List<ReceivedEventModelDto> {
        return RestClient(context).homeService.getReceivedUserEvents(
            authToken = PERSONAL_ACCESS_TOKEN,
            username = username,
            page = page,
            perPage = perPage
        )
    }

}