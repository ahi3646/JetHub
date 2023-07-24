package com.hasan.jetfasthub.screens.main.commits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.CommitRepository
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.CommitModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommitViewModel(private val repository: CommitRepository): ViewModel() {

    private var _state : MutableStateFlow<CommitScreenState> = MutableStateFlow(CommitScreenState())
    val state =  _state.asStateFlow()

    fun getCommit(
        token: String,
        owner: String,
        repo: String,
        branch: String,
    ) {
        viewModelScope.launch {
            try {
                repository.getCommit(token, owner, repo, branch).let { commit ->
                    if (commit.isSuccessful) {
                        _state.update {
                            it.copy(Commit = Resource.Success(commit.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(Commit = Resource.Failure(commit.errorBody().toString()))
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Commit = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

}

data class CommitScreenState(
    val Commit: Resource<CommitModel> = Resource.Loading()
)