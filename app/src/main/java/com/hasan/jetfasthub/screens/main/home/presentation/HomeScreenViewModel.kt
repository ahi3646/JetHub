package com.hasan.jetfasthub.screens.main.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.hasan.jetfasthub.core.ui.navigation.DefaultNavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.NavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.emitNavigation
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.RepoQueryProvider
import com.hasan.jetfasthub.screens.main.home.data.database.ReceivedEventsModelEntity
import com.hasan.jetfasthub.screens.main.home.domain.HomeUseCase
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.factory.HomeScreenFactory
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.data.mappers.toReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val homeUseCase: HomeUseCase,
    private val pager: Flow<PagingData<ReceivedEventsModelEntity>>,
) : ViewModel(), HomeScreenIntents, DefaultLifecycleObserver,
    NavigationEventDelegate<HomeScreenNavigation> by DefaultNavigationEventDelegate() {

    private val username = homeUseCase.getAuthenticatedUsername()

    private val stateFactory = HomeScreenFactory(
        currentStateProvider = Provider { uiState },
        clickIntents = this,
    )

    var uiState: HomeScreenStateConfig by mutableStateOf(stateFactory.getInitialState(username))
        private set

    init {
        refresh()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getUserData()
        //loadFeeds(pager)
        loadIssues()
        loadPullRequests()
    }

    override fun onIssueItemClick(issueOwner: String, issueRepo: String, issueNumber: String) {
        emitNavigation(HomeScreenNavigation.OpenIssueFragmentFragment(issueOwner, issueRepo, issueNumber))
    }

    private fun refresh(){
        viewModelScope.launch {
            loadFeeds(pager)
            uiState = stateFactory.getRefreshedState()
        }
    }

    override fun onRefreshSwipe() {
        Log.d("ahi3646", "onRefreshSwipe: ")
        //uiState = stateFactory.getRefreshingState()
        viewModelScope.launch(Dispatchers.IO) {
            async {
            loadFeeds(pager)
            }.await()
            uiState = stateFactory.getRefreshedState()
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            uiState = stateFactory.getUserData(homeUseCase.getAuthenticatedUserData())
        }
    }

    private fun loadFeeds(value:Flow<PagingData<ReceivedEventsModelEntity>>) {
        val events = value.map { pagingData ->
            pagingData.map {
                it.toReceivedEventsModel()
            }
        }
        uiState = stateFactory.getFeeds(events)
    }

    private fun loadIssues() {
        uiState.issuesScreenConfig.actionTabs.forEach {
            getIssuesWithType(
                query = getUrlForIssues(
                    issueType = it.config.type,
                    issueState = IssueState.Open,
                    login = username
                ),
                page = 1,
                issuesType = it.config.type
            )
        }
    }

    private fun loadPullRequests() {
        getPullsWithType(
            query = getUrlForPulls(
                issueState = IssueState.All,
                issueType = MyIssuesType.CREATED,
                login = username
            ),
            page = 1,
            issuesType = MyIssuesType.CREATED
        )
    }

    @Suppress("SameParameterValue")
    //TODO implement pagination for issues screen
    private fun getIssuesWithType(query: String, page: Int, issuesType: MyIssuesType) {
        viewModelScope.launch {
            uiState = stateFactory.getIssuesWithType(
                homeUseCase.getIssuesWithCount(query, page),
                issuesType
            )
        }
    }

    @Suppress("SameParameterValue")
    //TODO implement pagination for issues screen
    private fun getPullsWithType(query: String, page: Int, issuesType: MyIssuesType) {
        viewModelScope.launch {
            uiState = stateFactory.getPullRequestsWithType(
                homeUseCase.getPullsWithCount(query, page),
                issuesType
            )
        }
    }

    override fun onIssuesTabChange(tabIndex: Int) {
        uiState = stateFactory.updateIssuesTab(tabIndex)
    }

    override fun onPullsTabChange(tabIndex: Int) {
        uiState = stateFactory.updatePullsTab(tabIndex)
    }

    override fun onDrawerTabChange(tabIndex: Int) {
        uiState = stateFactory.updateDrawerTab(tabIndex)
    }

    private fun getUrlForPulls(
        issueType: MyIssuesType,
        issueState: IssueState,
        login: String
    ): String {
        when (issueType) {
            MyIssuesType.CREATED -> return RepoQueryProvider.getMyIssuesPullRequestQuery(
                login,
                issueState,
                true
            )

            MyIssuesType.ASSIGNED -> return RepoQueryProvider.getAssigned(login, issueState, true)
            MyIssuesType.MENTIONED -> return RepoQueryProvider.getMentioned(login, issueState, true)
            MyIssuesType.REVIEW -> return RepoQueryProvider.getReviewRequests(login, issueState)
            else -> {}
        }
        return RepoQueryProvider.getMyIssuesPullRequestQuery(login, issueState, false)
    }

    private fun getUrlForIssues(
        issueType: MyIssuesType,
        issueState: IssueState,
        login: String
    ): String {
        when (issueType) {
            MyIssuesType.CREATED -> return RepoQueryProvider.getMyIssuesPullRequestQuery(
                login,
                issueState,
                false
            )

            MyIssuesType.ASSIGNED -> return RepoQueryProvider.getAssigned(login, issueState, false)
            MyIssuesType.MENTIONED -> return RepoQueryProvider.getMentioned(
                login,
                issueState,
                false
            )

            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> return RepoQueryProvider.getParticipated(
                login,
                issueState,
                false
            )
        }
    }

    override fun onIssuesStateChanged(type: MyIssuesType, state: IssueState) {
        uiState = stateFactory.updateIssueTabState(type, state)
        getIssuesWithType(
            query = getUrlForIssues(
                issueType = type,
                issueState = state,
                login = "ahi3646"
            ),
            page = 1,
            issuesType = type
        )
    }

    override fun onPullRequestsStateChanged(type: MyIssuesType, state: IssueState) {
        uiState = stateFactory.updatePullsTabState(type, state)
        getPullsWithType(
            query = getUrlForPulls(
                issueState = state,
                issueType = type,
                login = "ahi3646"
            ),
            page = 1,
            issuesType = type
        )
    }

    override fun onLogoutClick() {
        uiState = stateFactory.getStateWithLogoutBottomSheet()
    }

    override fun onDismissBottomSheet() {
        uiState = stateFactory.getStateWithClosedBottomSheet()
    }

    override fun openProfileFragment(username: String, profileTabStartIndex: String) {
        emitNavigation(HomeScreenNavigation.OpenProfileFragment(username, profileTabStartIndex))
    }

    override fun onBottomBarItemClick(screen: AppScreens) {
        uiState = stateFactory.getBottomBarScreenState(screen)
    }

    override fun openPinnedFragment() {
        emitNavigation(HomeScreenNavigation.OpenPinnedFragment)
    }

    override fun openGistsFragment(username: String) {
        emitNavigation(HomeScreenNavigation.OpenGistsFragment(username))
    }

    override fun openRepositoryFragment(repoOwner: String, repoName: String) {
        emitNavigation(HomeScreenNavigation.OpenRepositoryFragment(repoOwner, repoName))
    }

    override fun openFaqFragment() {
        HomeScreenNavigation.OpenFaqFragment
    }

    override fun openSearchFragment() {
        emitNavigation(HomeScreenNavigation.OpenSearchFragment)
    }

    override fun openIssueFragment(issueOwner: String, issueRepo: String, issueNumber: String) {
        emitNavigation(HomeScreenNavigation.OpenIssueFragmentFragment(issueOwner, issueRepo, issueNumber))
    }

    override fun openSettingsFragment() {
        emitNavigation(HomeScreenNavigation.OpenSettingsFragment)
    }

    override fun openAboutFragment() {
        emitNavigation(HomeScreenNavigation.OpenAboutFragment)
    }

    override fun openNotificationFragment() {
        emitNavigation(HomeScreenNavigation.OpenNotificationFragment)
    }

}