package com.hasan.jetfasthub.data

import android.content.Context
import android.util.Log
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentPostResponse
import com.hasan.jetfasthub.screens.main.commits.models.comment_post_model.CommentRequestModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response

interface CommentRepository {
    suspend fun editComment(
        token: String,
        owner: String,
        repo: String,
        commentId: Int,
        body: String
    ): Response<CommentPostResponse>
}


class CommentRepositoryImpl(private val context: Context) : CommentRepository {

    override suspend fun editComment(
        token: String,
        owner: String,
        repo: String,
        commentId: Int,
        body: String
    ): Response<CommentPostResponse> {
        Log.d("ahi3646", "editComment: comment repo impl ")
        return RetrofitInstance(context).gitHubService.editComment(
            token = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            owner = owner,
            repo = repo,
            commentId = commentId,
            body = CommentRequestModel(body)
        )
    }

}