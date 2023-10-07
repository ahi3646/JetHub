package com.hasan.jetfasthub.screens.main.home.configs

import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens
import com.hasan.jetfasthub.screens.main.home.configs.state.DrawerBodyConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.DrawerScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenBottomSheetConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenPullToRefreshConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenTopAppBarConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar.BottomNavBarButtons
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar.BottomNavBarConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_sheet.HomeScreenBottomSheets
import com.hasan.jetfasthub.screens.main.home.configs.state.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.drawer.DrawerProfileConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.top_app_bar.HomeScreenTabConfig
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

object HomeScreenPreview {

    private val issuesTabs = persistentListOf(
        HomeScreenTabConfig.Created(
            onTabChange = { },
            onTabStateChange = {}
        ),
        HomeScreenTabConfig.Assigned(
            onTabChange = {},
            onTabStateChange = {}
        ),
        HomeScreenTabConfig.Mentioned(
            onTabChange = {},
            onTabStateChange = {}
        ),
        HomeScreenTabConfig.Participated(
            onTabChange = {},
            onTabStateChange = {}
        )
    )

    private val issueScreenPreview = IssuesScreenConfig.Loading(
        tabIndex = 0,
        actionTabs = issuesTabs
    )

    private val pullRequestsTabs = persistentListOf(
        HomeScreenTabConfig.Created(onTabChange = {}, onTabStateChange = {}),
        HomeScreenTabConfig.Assigned(onTabChange = {}, onTabStateChange = {}),
        HomeScreenTabConfig.Mentioned(onTabChange = {}, onTabStateChange = {}),
        HomeScreenTabConfig.ReviewRequest(onTabChange = {}, onTabStateChange = {}),
    )

    private val pullRequestsScreenPreview = PullRequestsScreenConfig.Loading(
        actionTabs = pullRequestsTabs
    )

    val drawerMenuPreview = persistentListOf(
        DrawerMenuConfig.Home(onClick = {}),
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
        isOpen = false,
        drawerHeaderConfig = DrawerHeaderConfig.Loading,
        drawerBodyConfig = DrawerBodyConfig(
            tabIndex = 0,
            drawerMenuConfig = drawerMenuPreview,
            drawerProfileConfig = drawerProfilePreview
        )
    )

    val topAppBarPreview: HomeScreenTopAppBarConfig = HomeScreenTopAppBarConfig(
        onDrawerClick = {},
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