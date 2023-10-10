package com.hasan.jetfasthub.screens.main.home.domain

import com.hasan.jetfasthub.screens.main.home.data.models.received_events_model_dto.ReceivedEventModelDto
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

interface HomeRepository {

    suspend fun getUser(): GitHubUser

    fun getAuthenticatedUsername(): String

    suspend fun getReceivedUserEvents(
        username: String,
        page: Int,
        perPage: Int
    ): List<ReceivedEventModelDto>

    suspend fun getIssuesWithCount(
        query: String,
        page: Int
    ): IssuesModel

    suspend fun getPullsWithCount(
        query: String,
        page: Int
    ): IssuesModel

}