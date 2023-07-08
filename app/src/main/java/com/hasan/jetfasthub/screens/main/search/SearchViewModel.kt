package com.hasan.jetfasthub.screens.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.SearchRepository
import com.hasan.jetfasthub.screens.main.search.models.code_model.CodeModel
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.screens.main.search.models.repository_model.RepositoryModel
import com.hasan.jetfasthub.screens.main.search.models.users_model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

    private var _state: MutableStateFlow<SearchScreenState> = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    fun searchRepositories(token: String, query: String, page: Long) {
        _state.update {
            it.copy(Repositories = SearchResource.Loading())
        }
        viewModelScope.launch {
            try {
                repository.searchRepositories(token, query, page).let { repositoryModel ->
                    if (repositoryModel.isSuccessful) {
                        _state.update {
                            it.copy(Repositories = SearchResource.Success(repositoryModel.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                Repositories = SearchResource.Failure(
                                    repositoryModel.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Repositories = SearchResource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun searchUsers(token: String, query: String, page: Long) {
        _state.update {
            it.copy(Users = SearchResource.Loading())
        }
        viewModelScope.launch {
            try {
                repository.searchUsers(token, query, page).let { userModel ->
                    if (userModel.isSuccessful) {
                        _state.update {
                            it.copy(Users = SearchResource.Success(userModel.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                Users = SearchResource.Failure(
                                    userModel.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Users = SearchResource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun searchIssues(token: String, query: String, page: Long) {
        _state.update {
            it.copy(Issues = SearchResource.Loading())
        }
        viewModelScope.launch {
            try {
                repository.searchIssues(token, query, page).let { issuesModel ->
                    if (issuesModel.isSuccessful) {
                        _state.update {
                            it.copy(Issues = SearchResource.Success(issuesModel.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                Issues = SearchResource.Failure(
                                    issuesModel.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Issues = SearchResource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun searchCodes(token: String, query: String, page: Long) {
        _state.update {
            it.copy(Codes = SearchResource.Loading())
        }
        viewModelScope.launch {
            try {
                repository.searchCodes(token, query, page).let { codeModelResponse ->
                    if (codeModelResponse.isSuccessful) {
                        _state.update {
                            it.copy(Codes = SearchResource.Success(codeModelResponse.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                Codes = SearchResource.Failure(
                                    codeModelResponse.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Codes = SearchResource.Failure(e.message.toString()))
                }
            }
        }
    }

}

//data class SearchScreenState(
//    val Repositories: Resource<RepositoryModel> = Resource.Loading(),
//    val Users: Resource<UserModel> = Resource.Loading(),
//    val Issues: Resource<IssuesModel> = Resource.Loading(),
//    val Codes: Resource<CodeModel> = Resource.Loading()
//)

data class SearchScreenState(
    val Repositories: SearchResource<RepositoryModel> = SearchResource.Initial(),
    val Users: SearchResource<UserModel> = SearchResource.Initial(),
    val Issues: SearchResource<IssuesModel> = SearchResource.Initial(),
    val Codes: SearchResource<CodeModel> = SearchResource.Initial()
)

sealed class SearchResource<T>(
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Initial<T> : SearchResource<T>()
    class Loading<T> : SearchResource<T>()
    class Success<T>(data:T) : SearchResource<T>()
    class Failure<T>(errorMessage: String?) : SearchResource<T>()
}