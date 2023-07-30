package com.hasan.jetfasthub.screens.main.gists

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.GistRepository
import com.hasan.jetfasthub.screens.main.gists.gist_comments_model.GistCommentsModel
import com.hasan.jetfasthub.screens.main.gists.gist_model.GistModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GistViewModel(private val repository: GistRepository): ViewModel() {

    private var _state: MutableStateFlow<GistScreenState> = MutableStateFlow(GistScreenState())
    val state = _state.asStateFlow()

    fun getGist(token: String, gistId: String) {
        viewModelScope.launch {
            try {
                repository.getGist(token, gistId).let {gistResponse->
                    if(gistResponse.isSuccessful){
                        _state.update {
                            it.copy(Gist = Resource.Success(gistResponse.body()!!) )
                        }
                    }else{
                        _state.update {
                            it.copy(Gist = Resource.Failure(gistResponse.errorBody().toString()) )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Gist = Resource.Failure(e.message.toString()) )
                }
            }
        }
    }

    fun getGistComments(token: String, gistId: String) {
        viewModelScope.launch {
            try {
                repository.getGistComments(token, gistId).let {gistCommentsResponse->
                    if(gistCommentsResponse.isSuccessful){
                        _state.update {
                            it.copy(GistComments = Resource.Success(gistCommentsResponse.body()!!) )
                        }
                    }else{
                        _state.update {
                            it.copy(GistComments = Resource.Failure(gistCommentsResponse.errorBody().toString()) )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(GistComments = Resource.Failure(e.message.toString()) )
                }
            }
        }
    }

    fun deleteGist(token: String, gistId: String): Flow<Boolean> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.deleteGist(token, gistId).let { response ->
                    if(response.code() == 204){
                        trySend(true)
                    }else{
                        trySend(false)
                    }
                }
            }catch (e: Exception){
                trySend(false)
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "deleteGist: channel closed ")
        }
    }

    fun starGist(token: String, gistId: String): Flow<Boolean> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.starGist(token, gistId).let { response ->
                    if(response.code() == 204){
                        _state.update {
                            it.copy(HasGistStarred = true)
                        }
                        trySend(true)
                    }else{
                        trySend(false)
                    }
                }
            }catch (e: Exception){
                trySend(false)
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "starGist: channel closed ")
        }
    }

    fun unstarGist(token: String, gistId: String): Flow<Boolean> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.unstarGist(token, gistId).let { response ->
                    if(response.code() == 204){
                        _state.update {
                            it.copy(HasGistStarred = false)
                        }
                        trySend(true)
                    }else{
                        trySend(false)
                    }
                }
            }catch (e: Exception){
                trySend(false)
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "unStarGist: channel closed ")
        }
    }

    fun changeForkStatus(){
        _state.update {
            it.copy(
                HasForked = false
            )
        }
    }

    fun forkGist(token: String, gistId: String): Flow<Boolean> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.forkGist(token, gistId).let { response ->
                    if (response.code() == 201) {
                        _state.update {
                            it.copy(HasForked = true)
                        }
                        trySend(true)
                    } else {
                        trySend(false)
                    }
                }
            } catch (e: Exception) {
                Log.d("ahi3646", "forkRepo: ${e.message} ")
                trySend(false)
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "forkGist: channel closed ")
        }
    }

    fun hasGistStarred(token: String, gistId: String) {
        Log.d("ahi3646", "hasGistStarred vm ")
        viewModelScope.launch {
            try {
                repository.checkIfGistStarred(token, gistId).let {response->
                    if(response.isSuccessful){
                        _state.update {
                            Log.d("ahi3646", "hasGistStarred: true ")
                            it.copy(HasGistStarred = true )
                        }
                    }else{
                        Log.d("ahi3646", "hasGistStarred vm else: ${response.errorBody().toString()} ")
                    }
                }
            } catch (e: Exception) {
                Log.d("ahi3646", "hasGistStarred: vm error - ${e.message.toString()} ")
            }
        }
    }

}

data class GistScreenState(
    val Gist: Resource<GistModel> = Resource.Loading(),
    val GistComments: Resource<GistCommentsModel> = Resource.Loading(),
    val HasGistStarred: Boolean = false,
    val HasForked: Boolean = false
)