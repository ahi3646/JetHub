package com.hasan.jetfasthub.screens.main.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.map
import com.hasan.jetfasthub.core.ui.navigation.DefaultNavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.NavigationEventDelegate
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.RepoQueryProvider
import com.hasan.jetfasthub.screens.main.home.data.database.ReceivedEventsModelEntity
import com.hasan.jetfasthub.screens.main.home.domain.HomeUseCase
import com.hasan.jetfasthub.screens.main.home.configs.Provider
import com.hasan.jetfasthub.screens.main.home.configs.factory.HomeScreenFactory
import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.data.mappers.toReceivedEventsModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val TAG = "ahi3646"

class HomeScreenViewModel(
    private val homeUseCase: HomeUseCase,
    private val pager: Pager<Int, ReceivedEventsModelEntity>
) : ViewModel(), HomeScreenIntents, DefaultLifecycleObserver,
    NavigationEventDelegate<HomeScreenNavigation> by DefaultNavigationEventDelegate() {

    //private var userData by Delegates.notNull<GitHubUser>()

    private val stateFactory = HomeScreenFactory(
        currentStateProvider = Provider { uiState },
        clickIntents = this,
    )

    var uiState: HomeScreenStateConfig by mutableStateOf(stateFactory.getInitialState())
        private set

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate: vm")
        getUserData()
        loadFeeds(pager)
        loadIssues()
        loadPullRequests()
    }

    override fun onRefreshSwipe() {
        Log.d(TAG, "onRefreshSwipe: ")
        viewModelScope.launch {
            loadFeeds(pager)
            uiState = stateFactory.getRefreshedState()
        }
    }

    override fun onDrawerClick() {
        Log.d(TAG, "onDrawerClick: ")
    }


    private fun getUserData() {
        viewModelScope.launch {
            homeUseCase.getAuthenticatedUserData().fold(
                ifLeft = {},
                ifRight = {
                    //userData = it
                    Log.d(TAG, "getUserData: $it ")
                }
            )
        }
    }

    private fun loadFeeds(value: Pager<Int, ReceivedEventsModelEntity>){
        val events = value.flow.map { pagingData ->
            pagingData.map {
                it.toReceivedEventsModel()
            }
        }
        uiState = stateFactory.getLoadedEvents(events)
    }

    private fun loadIssues(){
        Log.d(TAG, "loadIssues: ")
        getIssues(
            query = getUrlForIssues(
                issueType = MyIssuesType.CREATED,
                issueState = IssueState.Open,
                login = "ahi3646"
            ),
            page = 1,
            issuesType = MyIssuesType.CREATED
        )
    }

    private fun loadPullRequests() {
        Log.d(TAG, "loadPulls: ")
        getPullsWithCount(
            query = getUrlForPulls(
                issueState = IssueState.All,
                issueType = MyIssuesType.CREATED,
                login = "ahi3646"
            ),
            page = 1,
            issuesType = MyIssuesType.CREATED
        )
    }

    private fun getIssues(query: String, page: Int, issuesType: MyIssuesType) {
        viewModelScope.launch {
            uiState = stateFactory.getIssues(homeUseCase.getIssuesWithCount(query, page))
        }
    }

    override fun onTabChange(tabIndex: Int) {
        Log.d(TAG, "onTabChange: $tabIndex")
        uiState = stateFactory.changeIssuesTab(tabIndex)
    }

    override fun onTabStateChange(state: IssueState) {
        Log.d(TAG, "onTabStateChange: $state")
    }

    @Suppress("SameParameterValue")
    private fun getPullsWithCount(query: String, page: Int, issuesType: MyIssuesType) {
        viewModelScope.launch {
            uiState = stateFactory.getPullRequests(homeUseCase.getPullsWithCount(query, page))
        }
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

            MyIssuesType.PARTICIPATED -> return RepoQueryProvider.getParticipated(
                login,
                issueState,
                false
            )

            else -> {}
        }
        return RepoQueryProvider.getMyIssuesPullRequestQuery(login, issueState, false)
    }

    override fun onIssuesStateChanged(issueState: IssueState, issuesType: MyIssuesType) {
        Log.d(TAG, "onClick ")
    }

    override fun onPullRequestsStateChanged(
        pullState: IssueState,
        issuesType: MyIssuesType
    ) {
        Log.d(TAG, "onClick ")
    }

    override fun onLogoutClick() {
        Log.d(TAG, "onClick ")
    }

    override fun openProfileFragment(username: String, profileTabStartIndex: String) {
        Log.d(TAG, "onClick ")
    }

    override fun onBottomBarItemClick(screen: AppScreens) {
        uiState = stateFactory.getBottomBarScreenState(screen)
        Log.d(TAG, "onBottomBarItemClick: $screen")
    }

    override fun openPinnedFragment() {
        Log.d(TAG, "onClick ")
    }

    override fun openGistsFragment(username: String) {
        Log.d(TAG, "onClick ")
    }

    override fun openRepositoryFragment(repoOwner: String, repoName: String) {
        Log.d(TAG, "onClick ")
    }

    override fun openFaqFragment() {
        Log.d(TAG, "onClick ")
    }

    override fun openSearchFragment() {
        Log.d(TAG, "onClick ")
    }

    override fun openIssueFragment(issueOwner: String, issueRepo: String, issueNumber: String) {
        Log.d(TAG, "onClick ")
    }

    override fun openSettingsFragment() {
        Log.d(TAG, "onClick ")
    }

    override fun openAboutFragment() {
        Log.d(TAG, "onClick ")
    }

    override fun openNotificationFragment() {
        Log.d(TAG, "onClick ")
    }


}