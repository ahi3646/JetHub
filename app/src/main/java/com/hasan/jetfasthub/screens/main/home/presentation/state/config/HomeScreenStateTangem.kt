package com.hasan.jetfasthub.screens.main.home.presentation.state.config

import androidx.paging.PagingData
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerProfileConfig
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

data class DrawerScreenConfig(
    val isOpen: Boolean = false,
    val drawerHeaderConfig: DrawerHeaderConfig,
    val drawerBodyConfig: DrawerBodyConfig
)

sealed class DrawerHeaderConfig {
    data object Loading : DrawerHeaderConfig()
    data object Error : DrawerHeaderConfig()
    data class Content(val user: GitHubUser) : DrawerHeaderConfig()
}

data class DrawerBodyConfig(
    var tabIndex: Int = 0,
    val drawerTabs: List<Int> = listOf(R.string.menu_all_caps, R.string.profile_all_caps),
    val drawerMenuConfig: ImmutableList<DrawerMenuConfig>,
    val drawerProfileConfig: ImmutableList<DrawerProfileConfig>
)

sealed class IssuesScreenConfig {
    abstract var tabIndex: Int
    abstract val actionTabs: ImmutableList<HomeScreenTabConfig>

    data class Loading(
        override var tabIndex: Int,
        override val actionTabs: ImmutableList<HomeScreenTabConfig>
    ) :
        IssuesScreenConfig()

    data class Error(
        override var tabIndex: Int,
        override val actionTabs: ImmutableList<HomeScreenTabConfig>
    ) : IssuesScreenConfig()

    data class Content(
        override var tabIndex: Int,
        override val actionTabs: ImmutableList<HomeScreenTabConfig>,
        val issuesCreated: IssuesModel,
        val issuesMentioned: IssuesModel,
        val issuesAssigned: IssuesModel,
        val issuesParticipated: IssuesModel,
    ) : IssuesScreenConfig()

    fun copyTabIndex(index: Int): IssuesScreenConfig {
        return when (this) {
            is Content -> this.copy(tabIndex = index)
            is Error -> this.copy(tabIndex = index)
            is Loading -> this.copy(tabIndex = index)
        }
    }

    fun copyIssues(
        issuesModel: IssuesModel,
        type: MyIssuesType
    ): IssuesScreenConfig {
        return when (this){
            is Content -> {
                when (type) {
                    MyIssuesType.CREATED -> this.copy(issuesCreated = issuesModel)
                    MyIssuesType.ASSIGNED -> this.copy(issuesAssigned = issuesModel)
                    MyIssuesType.MENTIONED -> this.copy(issuesMentioned = issuesModel)
                    MyIssuesType.PARTICIPATED -> this.copy(issuesParticipated = issuesModel)
                    //this case will not be called
                    MyIssuesType.REVIEW -> this.copy(issuesParticipated = issuesModel)
                }
            }
            is Error -> this
            is Loading -> this
        }
    }

    fun copyTabState(tabIndex: Int, state: IssueState): IssuesScreenConfig {
        val tabs = this.actionTabs
        tabs.elementAt(tabIndex).config.state = state
        return when (this) {
            is Content -> this.copy(actionTabs = tabs)
            is Error -> this.copy(actionTabs = tabs)
            is Loading -> this.copy(actionTabs = tabs)
        }
    }
}

sealed class PullRequestsScreenConfig {
    abstract var tabIndex: Int
    abstract val actionTabs: ImmutableList<HomeScreenTabConfig>

    data class Loading(
        override var tabIndex: Int,
        override val actionTabs: ImmutableList<HomeScreenTabConfig>
    ) : PullRequestsScreenConfig()

    data class Error(
        override var tabIndex: Int,
        override val actionTabs: ImmutableList<HomeScreenTabConfig>
    ) : PullRequestsScreenConfig()

    data class Content(
        override var tabIndex: Int,
        override val actionTabs: ImmutableList<HomeScreenTabConfig>,
        val pullCreated: IssuesModel,
        val pullMentioned: IssuesModel,
        val pullAssigned: IssuesModel,
        val pullReviewRequest: IssuesModel
    ) : PullRequestsScreenConfig()

    fun copyActionButtons(buttons: ImmutableList<HomeScreenTabConfig>): PullRequestsScreenConfig {
        return when (this) {
            is Content -> this.copy(actionTabs = buttons)
            is Error -> this.copy(actionTabs = buttons)
            is Loading -> this.copy(actionTabs = buttons)
        }
    }
}

data class FeedsScreenConfig(
    val feeds: Flow<PagingData<ReceivedEventsModel>>,
    val pullToRefreshConfig: HomeScreenPullToRefreshConfig
)

data class HomeScreenPullToRefreshConfig(
    val isRefreshing: Boolean,
    val onRefresh: () -> Unit
)

//TODO move to single class file
/**
 * JetHub bottom sheet config
 *
 * @property isShow           flag that determine if bottom sheet is shown
 * @property onDismissRequest lambda be invoked when bottom sheet is dismissed
 * @property content          content config
 */
data class HomeScreenBottomSheetConfig(
    val isShow: Boolean,
    val onDismissRequest: () -> Unit,
    val content: HomeScreenBottomSheetConfigContent,
)

interface HomeScreenBottomSheetConfigContent

data class HomeScreenTopAppBarConfig(
    val onDrawerClick: () -> Unit,
    val onSearchClick: () -> Unit,
    val onNotificationClick: () -> Unit,
)

sealed interface AppScreens {
    data object Feeds : AppScreens
    data object Issues : AppScreens
    data object PullRequests : AppScreens
}


    