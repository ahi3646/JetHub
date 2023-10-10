package com.hasan.jetfasthub.screens.main.home.presentation.state

import com.hasan.jetfasthub.core.ui.utils.Resource
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenPullToRefreshConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenTopAppBarConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarButtons
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheetConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheets
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerBodyConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerProfileConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar.DrawerTabConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar.HomeScreenTabConfig
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

object HomeScreenPreview {

    private val issuesTabs = persistentListOf(
        HomeScreenTabConfig.Created(
            onTabChange = { },
            onTabStateChange = {_, _ -> }
        ),
        HomeScreenTabConfig.Assigned(
            onTabChange = {},
            onTabStateChange = {_, _ -> }
        ),
        HomeScreenTabConfig.Mentioned(
            onTabChange = {},
            onTabStateChange = {_, _ -> }
        ),
        HomeScreenTabConfig.Participated(
            onTabChange = {},
            onTabStateChange = {_, _ -> }
        )
    )

    private val issueScreenPreview = IssuesScreenConfig(
        tabIndex = 0,
        onIssueItemClick =  {_, _, _ -> },
        actionTabs = issuesTabs,
        issuesCreated = Resource.Loading(),
        issuesAssigned = Resource.Loading(),
        issuesMentioned = Resource.Loading(),
        issuesParticipated = Resource.Loading(),
    )

    val drawerTabs = persistentListOf(
        DrawerTabConfig.Menu(onTabChange = {}),
        DrawerTabConfig.Profile(onTabChange = {})
    )

    private val pullRequestsTabs = persistentListOf(
        HomeScreenTabConfig.Created(onTabChange = {}, onTabStateChange = {_, _ ->}),
        HomeScreenTabConfig.Assigned(onTabChange = {}, onTabStateChange = {_, _ ->}),
        HomeScreenTabConfig.Mentioned(onTabChange = {}, onTabStateChange = {_, _ ->}),
        HomeScreenTabConfig.ReviewRequest(onTabChange = {}, onTabStateChange = {_, _ ->}),
    )

    private val pullRequestsScreenPreview = PullRequestsScreenConfig(
        tabIndex = 0,
        actionTabs = pullRequestsTabs,
        pullCreated = Resource.Loading(),
        pullAssigned = Resource.Loading(),
        pullMentioned = Resource.Loading(),
        pullReviewRequest = Resource.Loading(),
    )

    val drawerMenuPreview = persistentListOf(
        DrawerMenuConfig.Profile(onClick = {}),
        DrawerMenuConfig.Organizations(onClick = {}),
        DrawerMenuConfig.Notifications(onClick = {}),
        DrawerMenuConfig.Pinned(onClick = {}),
        DrawerMenuConfig.Trending(onClick = {}),
        DrawerMenuConfig.Gists(onClick = {}),
        DrawerMenuConfig.JetHub(onClick = {}),
        DrawerMenuConfig.Faq(onClick = {}),
        DrawerMenuConfig.Settings(onClick = {}),
        DrawerMenuConfig.About(onClick = {}),
    )

    val drawerProfilePreview = persistentListOf(
        DrawerProfileConfig.Logout(onClick = {}),
        DrawerProfileConfig.AddAccount(onClick = {}),
        DrawerProfileConfig.Repositories(onClick = {}),
        DrawerProfileConfig.Starred(onClick = {}),
        DrawerProfileConfig.Pinned(onClick = {}),
    )

    private val drawerScreenConfig = DrawerScreenConfig(
        drawerHeaderConfig = DrawerHeaderConfig.Loading,
        drawerBodyConfig = DrawerBodyConfig(
            tabIndex = 0,
            drawerTabs = drawerTabs,
            drawerMenuConfig = drawerMenuPreview,
            drawerProfileConfig = drawerProfilePreview
        )
    )

    val topAppBarPreview: HomeScreenTopAppBarConfig = HomeScreenTopAppBarConfig(
        onSearchClick = {},
        onNotificationClick = {}
    )

    private val bottomNavBarButtons = persistentListOf(
        BottomNavBarButtons.Feeds(onClick = {}),
        BottomNavBarButtons.Issues(onClick = {}),
        BottomNavBarButtons.PullRequests(onClick = {})
    )

    val bottomNavPreview = BottomNavBarConfig(
        selectedBottomBarItem = AppScreens.Feeds,
        buttons = bottomNavBarButtons
    )

    val homeScreenPreview = HomeScreenStateConfig(
        topAppBarConfig = topAppBarPreview,
        bottomSheetConfig = HomeScreenBottomSheetConfig(
            isShow = false,
            onDismissRequest = {},
            content = HomeScreenBottomSheets.LogoutSheet(
                onRegret = {},
                onAccept = {}
            )
        ),
        feedsScreenConfig = FeedsScreenConfig(flowOf(), createPullToRefresh()),
        issuesScreenConfig = issueScreenPreview,
        pullRequestsScreenConfig = pullRequestsScreenPreview,
        drawerScreenConfig = drawerScreenConfig,
        bottomNavBarConfig = bottomNavPreview,
    )

    private fun createPullToRefresh(): HomeScreenPullToRefreshConfig {
        return HomeScreenPullToRefreshConfig(isRefreshing = false, onRefresh = {})
    }

}