package com.hasan.jetfasthub.screens.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.HomeRepository
import com.hasan.jetfasthub.screens.main.home.authenticated_user_model.AuthenticatedUser
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.screens.main.home.received_events_model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.utility.MyIssuesType
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    fun onBottomBarItemSelected(appScreens: AppScreens) {
        _state.update {
            it.copy(selectedBottomBarItem = appScreens)
        }
    }

    fun getIssuesWithCount(token: String, query: String, page: Int, issuesType: MyIssuesType) {

        viewModelScope.launch {
            try {
                repository.getIssuesWithCount(token, query, page).let { issuesResponse ->
                    if (issuesResponse.isSuccessful) {
                        when (issuesType) {
                            MyIssuesType.CREATED -> {
                                _state.update {
                                    it.copy(
                                        IssuesCreated = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.ASSIGNED -> {
                                _state.update {
                                    it.copy(
                                        IssuesAssigned = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.MENTIONED -> {
                                _state.update {
                                    it.copy(
                                        IssuesMentioned = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.PARTICIPATED -> {
                                _state.update {
                                    it.copy(
                                        IssuesParticipated = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            else -> {}
                        }
                    } else {
                        when (issuesType) {
                            MyIssuesType.CREATED -> {
                                _state.update {
                                    it.copy(
                                        IssuesCreated = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.ASSIGNED -> {
                                _state.update {
                                    it.copy(
                                        IssuesAssigned = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.MENTIONED -> {
                                _state.update {
                                    it.copy(
                                        IssuesMentioned = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.PARTICIPATED -> {
                                _state.update {
                                    it.copy(
                                        IssuesParticipated = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            } catch (e: Exception) {
                when (issuesType) {
                    MyIssuesType.CREATED -> {
                        _state.update {
                            it.copy(
                                IssuesCreated = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    MyIssuesType.ASSIGNED -> {
                        _state.update {
                            it.copy(
                                IssuesAssigned = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    MyIssuesType.MENTIONED -> {
                        _state.update {
                            it.copy(
                                IssuesMentioned = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    MyIssuesType.PARTICIPATED -> {
                        _state.update {
                            it.copy(
                                IssuesParticipated = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    else -> {}
                }
            }
        }
    }

    fun getAuthenticatedUser(token: String): Flow<AuthenticatedUser> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.getAuthenticatedUser(token).let { authenticatedUser ->
                    if (authenticatedUser.isSuccessful) {
                        trySend(authenticatedUser.body()!!)
                    } else {
                        _state.update {
                            it.copy(
                                user = Resource.Failure(
                                    authenticatedUser.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(user = Resource.Failure(e.message.toString()))
                }
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "getAuthenticatedUser: callback channel stopped ")
        }
    }

    fun getUser(token: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getUser(token, username).let { gitHubUser ->
                    if (gitHubUser.isSuccessful) {
                        _state.update {
                            it.copy(user = Resource.Success(gitHubUser.body()!!))
                        }
                    } else {
                        _state.update {
                            it.copy(user = Resource.Failure(gitHubUser.errorBody().toString()))
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(user = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

    fun getReceivedEvents(token: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getReceivedUserEvents(token, username).let { receivedEvents ->
                    if (receivedEvents.isSuccessful) {
                        _state.update {
                            it.copy(
                                receivedEventsState = ReceivedEventsState.Success(
                                    receivedEvents.body()!!
                                )
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                receivedEventsState = ReceivedEventsState.Error(
                                    receivedEvents.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        receivedEventsState = ReceivedEventsState.Error(
                            e.message.toString()
                        )
                    )
                }
            }
        }
    }
}

data class HomeScreenState(
    val user: Resource<GitHubUser> = Resource.Loading(),
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds,
    val receivedEventsState: ReceivedEventsState = ReceivedEventsState.Loading,
    val IssuesCreated: Resource<IssuesModel> = Resource.Loading(),
    val IssuesAssigned: Resource<IssuesModel> = Resource.Loading(),
    val IssuesMentioned: Resource<IssuesModel> = Resource.Loading(),
    val IssuesParticipated: Resource<IssuesModel> = Resource.Loading()
)

sealed interface AppScreens {
    object Feeds : AppScreens
    object Issues : AppScreens
    object PullRequests : AppScreens
}


sealed interface ReceivedEventsState {
    object Loading : ReceivedEventsState
    data class Success(val events: ReceivedEventsModel) : ReceivedEventsState
    data class Error(val message: String) : ReceivedEventsState
}