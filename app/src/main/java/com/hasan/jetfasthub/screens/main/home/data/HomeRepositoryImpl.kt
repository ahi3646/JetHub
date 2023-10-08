package com.hasan.jetfasthub.screens.main.home.data

import android.content.Context
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.networking.RestClient
import com.hasan.jetfasthub.screens.main.home.data.models.received_events_model_dto.ReceivedEventModelDto
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.home.domain.HomeRepository
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class HomeRepositoryImpl(private val context: Context,  private val preferences: PreferenceHelper) : HomeRepository {

    override fun getAuthenticatedUsername(): String = preferences.getAuthenticatedUsername()

    override suspend fun getPullsWithCount(
        query: String,
        page: Int
    ): IssuesModel {
        return RestClient(context).homeService.getPullsWithCount(
            authToken = preferences.getToken(),
            query = query,
            page = page,
        )
    }

    override suspend fun getIssuesWithCount(
        query: String,
        page: Int
    ): IssuesModel {
        return RestClient(context).homeService.getIssuesWithCount(
            authToken = preferences.getToken(),
            query = query,
            page = page,
        )
    }

    override suspend fun getUser(): GitHubUser {
        return RestClient(context = context).homeService.getUser(
            authToken = preferences.getToken(),
            username = preferences.getAuthenticatedUsername()
        )
    }

    override suspend fun getReceivedUserEvents(
        username: String, page: Int, perPage: Int
    ): List<ReceivedEventModelDto> {
        return RestClient(context).homeService.getReceivedUserEvents(
            authToken = preferences.getToken(),
            username = username,
            page = page,
            perPage = perPage
        )
    }

}