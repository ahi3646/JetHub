package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RestClient
import com.hasan.jetfasthub.screens.main.issue.comments_model.IssueCommentsModel
import com.hasan.jetfasthub.screens.main.issue.issue_model.IssueModel
import retrofit2.Response

interface IssueRepository {

    suspend fun getIssue(
        token: String,
        owner: String,
        repo: String,
        issueNumber: String
    ): Response<IssueModel>

    suspend fun getComments(
        token: String,
        owner: String,
        repo: String,
    ): Response<IssueCommentsModel>

}

class IssueRepositoryImpl(private val context: Context) : IssueRepository {

    override suspend fun getComments(
        token: String,
        owner: String,
        repo: String
    ): Response<IssueCommentsModel> {
        return RestClient(context).issueService.getComments(
            authToken = token,
            owner = owner,
            repo = repo,
        )
    }

    override suspend fun getIssue(
        token: String,
        owner: String,
        repo: String,
        issueNumber: String
    ): Response<IssueModel> {
        return RestClient(context).issueService.getIssue(
            authToken = token,
            owner = owner,
            repo = repo,
            issueNumber = issueNumber
        )
    }

}