package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenPullToRefreshConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenTopAppBarConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarButtons
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerProfileConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar.HomeScreenTabConfig
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerBodyConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar.DrawerTabConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

class HomeScreenStateConverter(
    private val clickIntents: HomeScreenIntents
) : Converter<String, HomeScreenStateConfig> {

    override fun convert(value: String): HomeScreenStateConfig {
        return HomeScreenStateConfig(
            topAppBarConfig = HomeScreenTopAppBarConfig(
                onNotificationClick = clickIntents::openNotificationFragment,
                onSearchClick = clickIntents::openSearchFragment
            ),
            bottomSheetConfig = null,
            feedsScreenConfig = FeedsScreenConfig(
                feeds = flowOf(),
                pullToRefreshConfig = createPullToRefresh()
            ),
            issuesScreenConfig = IssuesScreenConfig(
                tabIndex = 0,
                onIssueItemClick = clickIntents::onIssueItemClick,
                actionTabs = createActionTabsForIssues(),
                issuesCreated = Resource.Loading(),
                issuesAssigned = Resource.Loading(),
                issuesMentioned = Resource.Loading(),
                issuesParticipated = Resource.Loading(),
            ),
            pullRequestsScreenConfig = PullRequestsScreenConfig(
                tabIndex = 0,
                actionTabs = createActionTabsForPullRequests(),
                pullCreated = Resource.Loading(),
                pullAssigned = Resource.Loading(),
                pullMentioned = Resource.Loading(),
                pullReviewRequest= Resource.Loading(),
            ),
            drawerScreenConfig = DrawerScreenConfig(
                drawerHeaderConfig = DrawerHeaderConfig.Loading,
                drawerBodyConfig = DrawerBodyConfig(
                    tabIndex = 0,
                    drawerTabs = createDrawerTabs(),
                    drawerMenuConfig = createDrawerMenuConfig(value),
                    drawerProfileConfig = createDrawerProfileConfig(value),
                ),
            ),
            bottomNavBarConfig = BottomNavBarConfig(
                selectedBottomBarItem = AppScreens.Feeds,
                buttons = createBottomBarButtons()
            ),
        )
    }

    private fun createDrawerTabs(): ImmutableList<DrawerTabConfig>{
        return persistentListOf(
            DrawerTabConfig.Menu(onTabChange = clickIntents::onDrawerTabChange),
            DrawerTabConfig.Profile(onTabChange = clickIntents::onDrawerTabChange)
        )
    }

    private fun createPullToRefresh(): HomeScreenPullToRefreshConfig {
        return HomeScreenPullToRefreshConfig(
            isRefreshing = false,
            onRefresh = clickIntents::onRefreshSwipe
        )
    }

    private fun createDrawerMenuConfig(username: String): ImmutableList<DrawerMenuConfig> {
        return persistentListOf(
            DrawerMenuConfig.Profile(onClick = { clickIntents.openProfileFragment(username = username) }),
            DrawerMenuConfig.Organizations(onClick = {}),
            DrawerMenuConfig.Notifications(onClick = clickIntents::openNotificationFragment),
            DrawerMenuConfig.Pinned(onClick = clickIntents::openPinnedFragment),
            DrawerMenuConfig.Trending(onClick = {}),
            DrawerMenuConfig.Gists(onClick = { clickIntents.openGistsFragment(username = username) }),
            DrawerMenuConfig.JetHub(
                onClick = {
                    clickIntents.openRepositoryFragment(
                        Constants.JetHubOwner,
                        Constants.JetHubRepoName
                    )
                }
            ),
            DrawerMenuConfig.Faq(onClick = clickIntents::openFaqFragment),
            DrawerMenuConfig.Settings(onClick = clickIntents::openSettingsFragment),
            DrawerMenuConfig.About(onClick = clickIntents::openAboutFragment),
        )
    }

    private fun createDrawerProfileConfig(username: String): ImmutableList<DrawerProfileConfig> {
        return persistentListOf(
            DrawerProfileConfig.AddAccount(onClick = {}),
            DrawerProfileConfig.Pinned(onClick = clickIntents::openPinnedFragment),
            DrawerProfileConfig.Repositories(
                onClick = {
                    clickIntents.openProfileFragment(
                        username = username,
                        profileTabStartIndex = "2"
                    )
                }
            ),
            DrawerProfileConfig.Logout(
                onClick = {
                    clickIntents.onLogoutClick()
                }
            ),
        )
    }

    private fun createBottomBarButtons(): ImmutableList<BottomNavBarButtons> {
        return persistentListOf(
            BottomNavBarButtons.Feeds(onClick = clickIntents::onBottomBarItemClick),
            BottomNavBarButtons.Issues(onClick = clickIntents::onBottomBarItemClick),
            BottomNavBarButtons.PullRequests(onClick = clickIntents::onBottomBarItemClick),
        )
    }

    private fun createActionTabsForIssues(): ImmutableList<HomeScreenTabConfig> {
        return persistentListOf(
            HomeScreenTabConfig.Created(
                onTabChange = clickIntents::onIssuesTabChange,
                onTabStateChange = clickIntents::onIssuesStateChanged
            ),
            HomeScreenTabConfig.Assigned(
                onTabChange = clickIntents::onIssuesTabChange,
                onTabStateChange = clickIntents::onIssuesStateChanged
            ),
            HomeScreenTabConfig.Mentioned(
                onTabChange = clickIntents::onIssuesTabChange,
                onTabStateChange = clickIntents::onIssuesStateChanged
            ),
            HomeScreenTabConfig.Participated(
                onTabChange = clickIntents::onIssuesTabChange,
                onTabStateChange = clickIntents::onIssuesStateChanged
            )
        )
    }

    private fun createActionTabsForPullRequests(): ImmutableList<HomeScreenTabConfig> {
        return persistentListOf(
            HomeScreenTabConfig.Created(
                onTabChange = clickIntents::onPullsTabChange,
                onTabStateChange = clickIntents::onPullRequestsStateChanged
            ),
            HomeScreenTabConfig.Assigned(
                onTabChange = clickIntents::onPullsTabChange,
                onTabStateChange = clickIntents::onPullRequestsStateChanged
            ),
            HomeScreenTabConfig.Mentioned(
                onTabChange = clickIntents::onPullsTabChange,
                onTabStateChange = clickIntents::onPullRequestsStateChanged
            ),
            HomeScreenTabConfig.ReviewRequest(
                onTabChange = clickIntents::onPullsTabChange,
                onTabStateChange = clickIntents::onPullRequestsStateChanged
            )
        )
    }

}