package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentRequestModel
import com.hasan.jetfasthub.screens.main.gists.fork_response_model.GistForkResponse
import com.hasan.jetfasthub.screens.main.gists.gist_comment_response.GistCommentResponse
import com.hasan.jetfasthub.screens.main.gists.gist_comments_model.GistCommentsModel
import com.hasan.jetfasthub.screens.main.gists.gist_model.GistModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response


interface GistRepository {

    suspend fun getGist(token: String, gistId: String): Response<GistModel>

    suspend fun getGistComments(token: String, gistId: String): Response<GistCommentsModel>

    suspend fun deleteGist(token: String, gistId: String): Response<Boolean>

    suspend fun postGistComment(
        token: String,
        gistId: String,
        body: String
    ): Response<GistCommentResponse>

    suspend fun deleteGistComment(
        token: String,
        gistId: String,
        commentId: Int
    ): Response<Boolean>

    suspend fun checkIfGistStarred(token: String, gistId: String): Response<Boolean>

    suspend fun forkGist(token: String, gistId: String): Response<GistForkResponse>

    suspend fun starGist(token: String, gistId: String): Response<Boolean>

    suspend fun unstarGist(token: String, gistId: String): Response<Boolean>

}

class GistRepositoryImpl(private val context: Context) : GistRepository {

    override suspend fun deleteGistComment(
        token: String,
        gistId: String,
        commentId: Int
    ): Response<Boolean> {
        return RetrofitInstance(context).gitHubService.deleteGistComment(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId,
            commentId = commentId
        )
    }

    override suspend fun postGistComment(
        token: String,
        gistId: String,
        body: String
    ): Response<GistCommentResponse> {
        return RetrofitInstance(context).gitHubService.postGistComment(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            gistId = gistId,
            body = CommentRequestModel(body = body)
        )
    }

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

    override suspend fun getGistComments(
        token: String,
        gistId: String
    ): Response<GistCommentsModel> {
        return RetrofitInstance(context).gitHubService.getGistComments(
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