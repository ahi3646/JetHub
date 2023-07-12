package com.hasan.jetfasthub.screens.main.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.Repository
import com.hasan.jetfasthub.screens.main.repository.models.repo_model.RepoModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepositoryViewModel(private val repository: Repository): ViewModel() {

    private var _state: MutableStateFlow<RepositoryScreenState> = MutableStateFlow(
        RepositoryScreenState()
    )
    val state = _state.asStateFlow()

    fun onBottomBarItemClicked(repositoryScreen: RepositoryScreens){
        _state.update {
            it.copy(selectedBottomBarItem = repositoryScreen)
        }
    }

    fun getRepo(token: String, owner: String, repo: String){
        viewModelScope.launch {
            try {
                repository.getRepo(token, owner, repo).let { repo ->
                    if (repo.isSuccessful){
                        _state.update {
                            it.copy(repo = Resource.Success(repo.body()!!))
                        }
                    }else{
                        _state.update {
                            it.copy(repo = Resource.Failure(repo.errorBody().toString()))
                        }
                    }
                }
            }catch (e: Exception){
                _state.update {
                    it.copy(repo = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

}

data class RepositoryScreenState(
    val selectedBottomBarItem: RepositoryScreens = RepositoryScreens.Code,
    val repo: Resource<RepoModel> = Resource.Loading()
)

interface RepositoryScreens {

    object Code : RepositoryScreens

    object Issues : RepositoryScreens

    object PullRequest : RepositoryScreens

    object Projects : RepositoryScreens

}