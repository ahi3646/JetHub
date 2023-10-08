package com.hasan.jetfasthub.screens.main.home.presentation.state.config

import androidx.paging.PagingData
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheetConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar.HomeScreenTabConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

data class HomeScreenStateConfig(
    val topAppBarConfig: HomeScreenTopAppBarConfig,
    val drawerScreenConfig: DrawerScreenConfig,
    val feedsScreenConfig: FeedsScreenConfig,
    val issuesScreenConfig: IssuesScreenConfig,
    val pullRequestsScreenConfig: PullRequestsScreenConfig,
    val bottomNavBarConfig: BottomNavBarConfig,
    val bottomSheetConfig: HomeScreenBottomSheetConfig?
)

data class HomeScreenTopAppBarConfig(
    val onSearchClick: () -> Unit,
    val onNotificationClick: () -> Unit,
)

data class FeedsScreenConfig(
    val feeds: Flow<PagingData<ReceivedEventsModel>>,
    val pullToRefreshConfig: HomeScreenPullToRefreshConfig
){
    fun copyFeeds(newFeeds: Flow<PagingData<ReceivedEventsModel>>): FeedsScreenConfig{
        return this.copy(feeds = newFeeds)
    }
}

data class HomeScreenPullToRefreshConfig(
    val isRefreshing: Boolean,
    val onRefresh: () -> Unit
)

data class IssuesScreenConfig(
    val tabIndex: Int,
    val onIssueItemClick: (String, String, String)-> Unit,
    val actionTabs: ImmutableList<HomeScreenTabConfig>,
    val issuesCreated: Resource<IssuesModel>,
    val issuesMentioned: Resource<IssuesModel>,
    val issuesAssigned: Resource<IssuesModel>,
    val issuesParticipated: Resource<IssuesModel>,
) {

    fun copyIssues(issuesModel: IssuesModel, type: MyIssuesType): IssuesScreenConfig {
        return when (type) {
            MyIssuesType.CREATED -> this.copy(issuesCreated = Resource.Success(issuesModel))
            MyIssuesType.ASSIGNED -> this.copy(issuesAssigned = Resource.Success(issuesModel))
            MyIssuesType.MENTIONED -> this.copy(issuesMentioned = Resource.Success(issuesModel))
            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> this.copy(
                issuesParticipated = Resource.Success(
                    issuesModel
                )
            )
        }
    }

    fun copyIssuesOnError(error: NetworkErrors, type: MyIssuesType): IssuesScreenConfig {
        return when (type) {
            MyIssuesType.CREATED -> this.copy(issuesCreated = Resource.Failure("Network error occurred - $error"))
            MyIssuesType.ASSIGNED -> this.copy(issuesAssigned = Resource.Failure("Network error occurred - $error"))
            MyIssuesType.MENTIONED -> this.copy(issuesMentioned = Resource.Failure("Network error occurred - $error"))
            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> this.copy(
                issuesParticipated = Resource.Failure("Network error occurred - $error")
            )
        }
    }

    fun copyTabState(type: MyIssuesType, state: IssueState): IssuesScreenConfig {
        val tabs = this.actionTabs
        val index = when (type) {
            MyIssuesType.CREATED -> 0
            MyIssuesType.ASSIGNED -> 1
            MyIssuesType.MENTIONED -> 2
            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> 3
        }
        tabs.elementAt(index).config.state = state
        return this.copy(actionTabs = tabs)
    }

    fun copyTabIndex(index: Int): IssuesScreenConfig {
        return this.copy(tabIndex = index)
    }

}

data class PullRequestsScreenConfig(
    val tabIndex: Int,
    val actionTabs: ImmutableList<HomeScreenTabConfig>,
    val pullCreated: Resource<IssuesModel>,
    val pullAssigned: Resource<IssuesModel>,
    val pullMentioned: Resource<IssuesModel>,
    val pullReviewRequest: Resource<IssuesModel>
) {

    fun copyPulls(issuesModel: IssuesModel, type: MyIssuesType): PullRequestsScreenConfig {
        return when (type) {
            MyIssuesType.CREATED -> this.copy(pullCreated = Resource.Success(issuesModel))
            MyIssuesType.ASSIGNED -> this.copy(pullAssigned = Resource.Success(issuesModel))
            MyIssuesType.MENTIONED -> this.copy(pullMentioned = Resource.Success(issuesModel))
            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> this.copy(
                pullReviewRequest = Resource.Success(
                    issuesModel
                )
            )
        }
    }

    fun copyPullsOnError(error: NetworkErrors, type: MyIssuesType): PullRequestsScreenConfig {
        return when (type) {
            MyIssuesType.CREATED -> this.copy(pullCreated = Resource.Failure("Network error occurred - $error"))
            MyIssuesType.ASSIGNED -> this.copy(pullAssigned = Resource.Failure("Network error occurred - $error"))
            MyIssuesType.MENTIONED -> this.copy(pullMentioned = Resource.Failure("Network error occurred - $error"))
            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> this.copy(
                pullReviewRequest = Resource.Failure("Network error occurred - $error")
            )
        }
    }

    fun copyTabState(type: MyIssuesType, state: IssueState): PullRequestsScreenConfig {
        val tabs = this.actionTabs
        val index = when (type) {
            MyIssuesType.CREATED -> 0
            MyIssuesType.ASSIGNED -> 1
            MyIssuesType.MENTIONED -> 2
            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> 3
        }
        tabs.elementAt(index).config.state = state
        return this.copy(actionTabs = tabs)
    }

    fun copyTabIndex(index: Int): PullRequestsScreenConfig {
        return this.copy(tabIndex = index)
    }

}
