package com.hasan.jetfasthub.screens.main.home.presentation

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.HomeRepository
import com.hasan.jetfasthub.screens.main.home.data.database.ReceivedEventsModelEntity
import com.hasan.jetfasthub.screens.main.home.data.mappers.toReceivedEventsModel
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val savedStateHandle: SavedStateHandle,
    private val repository: HomeRepository,
    private val pager: Pager<Int, ReceivedEventsModelEntity>
) : ViewModel(), DefaultLifecycleObserver {

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    //rethink about this later
    init {
        refresh()
    }

    fun refresh() {
        // Don't set _isRefreshing to true here as this will be called on init,
        //  the pull to refresh api will handle setting _isRefreshing to true
        viewModelScope.launch {
            getEvents()
            // Set _isRefreshing to false to indicate the refresh is complete
            _isRefreshing.emit(false)
        }
    }

    fun getEvents() {
        _state.update {
            it.copy(
                events = pager
                    .flow
                    .map { pagingData ->
                        pagingData.map { receivedEventsModelEntity ->
                            receivedEventsModelEntity.toReceivedEventsModel()
                        }
                    }
                    .cachedIn(viewModelScope)
            )
        }
    }

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
                                        issuesCreated = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.ASSIGNED -> {
                                _state.update {
                                    it.copy(
                                        issuesAssigned = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.MENTIONED -> {
                                _state.update {
                                    it.copy(
                                        issuesMentioned = Resource.Success(issuesResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.PARTICIPATED -> {
                                _state.update {
                                    it.copy(
                                        issuesParticipated = Resource.Success(issuesResponse.body()!!)
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
                                        issuesCreated = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.ASSIGNED -> {
                                _state.update {
                                    it.copy(
                                        issuesAssigned = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.MENTIONED -> {
                                _state.update {
                                    it.copy(
                                        issuesMentioned = Resource.Failure(
                                            issuesResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.PARTICIPATED -> {
                                _state.update {
                                    it.copy(
                                        issuesParticipated = Resource.Failure(
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
                                issuesCreated = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    MyIssuesType.ASSIGNED -> {
                        _state.update {
                            it.copy(
                                issuesAssigned = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    MyIssuesType.MENTIONED -> {
                        _state.update {
                            it.copy(
                                issuesMentioned = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getIssuesWithCount: ${e.message} ")
                    }

                    MyIssuesType.PARTICIPATED -> {
                        _state.update {
                            it.copy(
                                issuesParticipated = Resource.Failure(
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

    fun getPullsWithCount(token: String, query: String, page: Int, issuesType: MyIssuesType) {
        viewModelScope.launch {
            try {
                repository.getIssuesWithCount(token, query, page).let { pullResponse ->
                    if (pullResponse.isSuccessful) {
                        when (issuesType) {
                            MyIssuesType.CREATED -> {
                                _state.update {
                                    it.copy(
                                        pullsCreated = Resource.Success(pullResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.ASSIGNED -> {
                                _state.update {
                                    it.copy(
                                        pullsAssigned = Resource.Success(pullResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.MENTIONED -> {
                                _state.update {
                                    it.copy(
                                        pullsMentioned = Resource.Success(pullResponse.body()!!)
                                    )
                                }
                            }

                            MyIssuesType.REVIEW -> {
                                _state.update {
                                    it.copy(
                                        pullsReview = Resource.Success(pullResponse.body()!!)
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
                                        pullsCreated = Resource.Failure(
                                            pullResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.ASSIGNED -> {
                                _state.update {
                                    it.copy(
                                        pullsAssigned = Resource.Failure(
                                            pullResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.MENTIONED -> {
                                _state.update {
                                    it.copy(
                                        pullsMentioned = Resource.Failure(
                                            pullResponse.errorBody().toString()
                                        )
                                    )
                                }
                            }

                            MyIssuesType.REVIEW -> {
                                _state.update {
                                    it.copy(
                                        pullsReview = Resource.Failure(
                                            pullResponse.errorBody().toString()
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
                                pullsCreated = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getPullsWithCount: ${e.message} ")
                    }

                    MyIssuesType.ASSIGNED -> {
                        _state.update {
                            it.copy(
                                pullsAssigned = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getPullsWithCount: ${e.message} ")
                    }

                    MyIssuesType.MENTIONED -> {
                        _state.update {
                            it.copy(
                                pullsMentioned = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getPullsWithCount: ${e.message} ")
                    }

                    MyIssuesType.REVIEW -> {
                        _state.update {
                            it.copy(
                                pullsReview = Resource.Failure(
                                    e.message.toString()
                                )
                            )
                        }
                        Log.d("ahi3646", "getPullsWithCount: ${e.message} ")
                    }

                    else -> {}
                }
            }
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

    fun updateIssueScreen(index: Int, isOpen: Boolean) {
        val newState = state.value.issueScreenState.toMutableList()
        newState[index] = isOpen
        _state.update {
            it.copy(
                issueScreenState = newState
            )
        }
    }

    fun updatePullScreen(index: Int, isOpen: Boolean) {
        val newState = state.value.pullScreenState.toMutableList()
        newState[index] = isOpen
        _state.update {
            it.copy(
                pullScreenState = newState
            )
        }
    }
}

data class HomeScreenStateX(
    val drawerState: DrawerState,
    val bottomBarState: HomeBottomBarState,
    val pullToRefreshState: HomeScreenPullToRefreshState
)

data class HomeScreenPullToRefreshState(
    val isRefreshing: Boolean,
    val onRefresh: () -> Unit
)

sealed class DrawerState {
    data object Loading : DrawerState()
    data object Error : DrawerState()
    data class Success(val user: GitHubUser) : DrawerState()
}

data class HomeBottomBarState(
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds,
    val feedsScreenState: FeedsScreenState,
    val issuesScreenState: IssuesScreenState,
    val pullRequestsScreenState: PullRequestsScreenState,
)

sealed class PullRequestsScreenState {
    data object Loading : PullRequestsScreenState()
    data object Error : PullRequestsScreenState()
    data class Success(
        @DrawableRes val iconId: Int = R.drawable.ic_pull_requests,
        val title: Int = R.string.pull_requests,
        val pullScreenState: List<Boolean> = listOf(true, true, true, true),
        val pullsCreated: IssuesModel,
        val pullsAssigned: IssuesModel,
        val pullsMentioned: IssuesModel,
        val pullsReview: IssuesModel,
    ) : PullRequestsScreenState()
}

sealed class IssuesScreenState {
    data object Loading : IssuesScreenState()
    data object Error : IssuesScreenState()
    data class Success(
        @DrawableRes val iconId: Int = R.drawable.ic_info_outline,
        val title: Int = R.string.issues,
        val issueScreenState: List<Boolean> = listOf(true, true, true, true),
        val issuesCreated: IssuesModel,
        val issuesAssigned: IssuesModel,
        val issuesMentioned: IssuesModel,
        val issuesParticipated: IssuesModel,
    ) : IssuesScreenState()
}

sealed class FeedsScreenState {
    data object Loading : FeedsScreenState()
    data object Error : FeedsScreenState()
    data class Success(
        @DrawableRes val iconId: Int = R.drawable.ic_github,
        val title: Int = R.string.feeds,
        val feeds: Flow<PagingData<ReceivedEventsModel>>,
    ) : FeedsScreenState()
}


data class HomeScreenState(
    val events: Flow<PagingData<ReceivedEventsModel>> = flowOf(),
    val user: Resource<GitHubUser> = Resource.Loading(),
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds,
    val issuesCreated: Resource<IssuesModel> = Resource.Loading(),
    val issuesAssigned: Resource<IssuesModel> = Resource.Loading(),
    val issuesMentioned: Resource<IssuesModel> = Resource.Loading(),
    val issuesParticipated: Resource<IssuesModel> = Resource.Loading(),
    val pullsCreated: Resource<IssuesModel> = Resource.Loading(),
    val pullsAssigned: Resource<IssuesModel> = Resource.Loading(),
    val pullsMentioned: Resource<IssuesModel> = Resource.Loading(),
    val pullsReview: Resource<IssuesModel> = Resource.Loading(),
    val pullScreenState: List<Boolean> = listOf(true, true, true, true),
    val issueScreenState: List<Boolean> = listOf(true, true, true, true)
)

sealed interface AppScreens {
    data object Feeds : AppScreens
    data object Issues : AppScreens
    data object PullRequests : AppScreens
}


sealed interface ReceivedEventsState {
    object Loading : ReceivedEventsState
    data class Success(val events: List<ReceivedEventsModel>) : ReceivedEventsState
    data class Error(val message: String) : ReceivedEventsState
}


/**
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
 */

/**
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
 */