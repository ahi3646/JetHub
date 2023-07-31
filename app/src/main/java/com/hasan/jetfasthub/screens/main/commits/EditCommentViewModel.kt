package com.hasan.jetfasthub.screens.main.commits

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.CommentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class EditCommentViewModel(private val repository: CommentRepository) : ViewModel() {

    fun editComment(
        token: String,
        owner: String,
        repo: String,
        commentId: Int,
        body: String
    ): Flow<Boolean> = callbackFlow {
        Log.d("ahi3646", "editComment: vm ")
        viewModelScope.launch {
            try {
                repository.editComment(token, owner, repo, commentId, body).let { response ->
                    if (response.code() == 200) {
                        Log.d("ahi3646", "editComment: vm 1 ")
                        trySend(true)
                    } else {
                        Log.d("ahi3646", "editComment: vm 2 - ${response.errorBody().toString()}")
                        trySend(false)
                    }
                }
            } catch (e: Exception) {
                Log.d("ahi3646", "editComment: vm 3 - ${e.message.toString()}")
                trySend(false)
            }
        }
        Log.d("ahi3646", "editComment: vm trg ")
        awaitClose {
            channel.close()
            Log.d("ahi3646", "editCommitComment: channel closed ")
        }
    }

}