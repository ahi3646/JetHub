package com.hasan.jetfasthub.screens.main.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasan.jetfasthub.data.Repository
import com.hasan.jetfasthub.screens.main.repository.models.releases_model.ReleasesModel
import com.hasan.jetfasthub.screens.main.repository.models.releases_model.ReleasesModelItem
import com.hasan.jetfasthub.screens.main.repository.models.repo_contributor_model.Contributors
import com.hasan.jetfasthub.screens.main.repository.models.repo_model.RepoModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepositoryViewModel(private val repository: Repository) : ViewModel() {

    private var _state: MutableStateFlow<RepositoryScreenState> = MutableStateFlow(
        RepositoryScreenState()
    )
    val state = _state.asStateFlow()


    fun onBottomBarItemClicked(repositoryScreen: RepositoryScreens) {
        _state.update {
            it.copy(selectedBottomBarItem = repositoryScreen)
        }
    }

    fun onBottomSheetChanged(bottomSheet: BottomSheetScreens) {
        _state.update {
            it.copy(currentSheet = bottomSheet)
        }
    }

    fun getRepo(token: String, owner: String, repo: String) {
        viewModelScope.launch {
            try {
                repository.getRepo(token, owner, repo).let { repo ->
                    if (repo.isSuccessful) {
                        _state.update {
                            it.copy(repo = Resource.Success(repo.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(repo = Resource.Failure(repo.errorBody().toString()))
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(repo = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun getContributors(token: String, owner: String, repo: String, page: Int) {
        viewModelScope.launch {
            try {
                repository.getContributors(token, owner, repo, page).let { contributorsResponse ->
                    if (contributorsResponse.isSuccessful) {
                        _state.update {
                            it.copy(Contributors = Resource.Success(contributorsResponse.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                Contributors = Resource.Failure(
                                    contributorsResponse.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Contributors = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun getReleases(token: String, owner: String, repo: String, page: Int) {
        viewModelScope.launch {
            try {
                repository.getReleases(token, owner, repo, page).let { releasesModelResponse ->
                    if (releasesModelResponse.isSuccessful) {
                        _state.update {
                            it.copy(Releases = Resource.Success(releasesModelResponse.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                Releases = Resource.Failure(
                                    releasesModelResponse.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(Releases = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun getReadmeAsHtml(token: String, url: String) {
        viewModelScope.launch {
            try {
                repository.getReadmeAsHtml(token, url).let { readmeAsHtml ->
                    if (readmeAsHtml.isSuccessful) {
                        _state.update {
                            it.copy(ReadmeHtml = Resource.Success(readmeAsHtml.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(
                                ReadmeHtml = Resource.Failure(
                                    readmeAsHtml.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                _state.update {
                    it.copy(ReadmeHtml = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

}

data class RepositoryScreenState(
    val selectedBottomBarItem: RepositoryScreens = RepositoryScreens.Code,
    val repo: Resource<RepoModel> = Resource.Loading(),
    val Contributors: Resource<Contributors> = Resource.Loading(),
    val Releases: Resource<ReleasesModel> = Resource.Loading(),
    val currentSheet: BottomSheetScreens = BottomSheetScreens.RepositoryInfoSheet,
    val ReadmeHtml: Resource<String> = Resource.Loading()
)

sealed interface BottomSheetScreens {
    object RepositoryInfoSheet : BottomSheetScreens
    class ReleaseItemSheet(val releaseItem: ReleasesModelItem) : BottomSheetScreens
}

interface RepositoryScreens {

    object Code : RepositoryScreens

    object Issues : RepositoryScreens

    object PullRequest : RepositoryScreens

    object Projects : RepositoryScreens

}