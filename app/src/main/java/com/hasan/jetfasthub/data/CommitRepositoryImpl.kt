package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RestClient
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentPostResponse
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentRequestModel
import com.hasan.jetfasthub.screens.main.commits.models.commit_comments_model.CommitCommentsModel
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.CommitModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response


interface CommitRepository {

    suspend fun getCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ): Response<CommitModel>

    suspend fun getCommitComments(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ): Response<CommitCommentsModel>

    suspend fun postCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
        body: String
    ): Response<CommentPostResponse>

    suspend fun deleteComment(
        token: String,
        owner: String,
        repo: String,
        commentId: Int
    ): Response<Boolean>

}

class CommitRepositoryImpl(private val context: Context) : CommitRepository {

    override suspend fun deleteComment(
        token: String,
        owner: String,
        repo: String,
        commentId: Int
    ): Response<Boolean> {
        return RestClient(context).gitHubService.deleteComment(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            commentId = commentId
        )
    }

    override suspend fun postCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
        body: String
    ): Response<CommentPostResponse> {
        return RestClient(context).gitHubService.postCommitComment(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            ref = branch,
            body = CommentRequestModel(body)
        )
    }

    override suspend fun getCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ): Response<CommitModel> {
        return RestClient(context).gitHubService.getCommit(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            branch = branch,
        )
    }

    override suspend fun getCommitComments(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ): Response<CommitCommentsModel> {
        return RestClient(context).gitHubService.getCommitComments(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            ref = branch,
        )
    }

}