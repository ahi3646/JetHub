package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentPostResponse
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentRequestModel
import com.hasan.jetfasthub.screens.main.commits.models.commit_comments_model.CommitCommentsModel
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

}

class CommitRepositoryImpl(private val context: Context): CommitRepository {

    override suspend fun postCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
        body: String
    ): Response<CommentPostResponse> {
        return RetrofitInstance(context).gitHubService.postCommitComment(
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
        return RetrofitInstance(context).gitHubService.getCommit(
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
        return RetrofitInstance(context).gitHubService.getCommitComments(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            ref = branch,
        )
    }

}