package com.hasan.jetfasthub.screens.main.home.configs.converters

import android.util.Log
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens
import com.hasan.jetfasthub.screens.main.home.configs.state.DrawerBodyConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.DrawerScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenPullToRefreshConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenTopAppBarConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar.BottomNavBarButtons
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar.BottomNavBarConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.drawer.DrawerProfileConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.top_app_bar.HomeScreenTabConfig
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

class HomeScreenStateConverter(
    private val clickIntents: HomeScreenIntents
) : Converter<String, HomeScreenStateConfig> {

    override fun convert(value: String): HomeScreenStateConfig {
        return HomeScreenStateConfig(
            topAppBarConfig = HomeScreenTopAppBarConfig(
                onDrawerClick = {
                    Log.d("tangem3646", "convert: ")
                    clickIntents.onDrawerClick()
                },
                onNotificationClick = clickIntents::openNotificationFragment,
                onSearchClick = clickIntents::openSearchFragment
            ),
            bottomSheetConfig = null,
            feedsScreenConfig = FeedsScreenConfig(
                feeds = flowOf(),
                pullToRefreshConfig = createPullToRefresh()
            ),
            issuesScreenConfig = IssuesScreenConfig.Loading(
                tabIndex = 1,
                actionTabs = createActionTabsForIssues()
            ),
            pullRequestsScreenConfig = PullRequestsScreenConfig.Loading(actionTabs = createActionTabsForPullRequests()),
            drawerScreenConfig = DrawerScreenConfig(
                isOpen = false,
                drawerHeaderConfig = DrawerHeaderConfig.Loading,
                drawerBodyConfig = DrawerBodyConfig(
                    tabIndex = 0,
                    drawerMenuConfig = createDrawerMenuConfig(),
                    drawerProfileConfig = createDrawerProfileConfig(),
                ),
            ),
            bottomNavBarConfig = BottomNavBarConfig(
                selectedBottomBarItem = AppScreens.Feeds,
                buttons = createBottomBarButtons()
            ),
        )
    }

    private fun createPullToRefresh(): HomeScreenPullToRefreshConfig =
        HomeScreenPullToRefreshConfig(
            isRefreshing = false,
            onRefresh = clickIntents::onRefreshSwipe
        )

    private fun createDrawerMenuConfig(): ImmutableList<DrawerMenuConfig> {
        return persistentListOf(
            DrawerMenuConfig.Home(onClick = clickIntents::onDrawerClick),
            DrawerMenuConfig.Profile(onClick = { clickIntents.openProfileFragment("ahi3646") }),
            DrawerMenuConfig.Organizations(onClick = {}),
            DrawerMenuConfig.Notifications(onClick = clickIntents::openNotificationFragment),
            DrawerMenuConfig.Pinned(onClick = clickIntents::openPinnedFragment),
            DrawerMenuConfig.Trending(onClick = {}),
            DrawerMenuConfig.Gists(onClick = { clickIntents.openGistsFragment("ahi3646") }),
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

    private fun createDrawerProfileConfig(): ImmutableList<DrawerProfileConfig> {
        return persistentListOf(
            DrawerProfileConfig.AddAccount(onClick = {}),
            DrawerProfileConfig.Pinned(onClick = clickIntents::openPinnedFragment),
            DrawerProfileConfig.Repositories(
                onClick = {
                    clickIntents.openProfileFragment(
                        "ahi3646",
                        "2"
                    )
                }
            ),
            DrawerProfileConfig.Logout(
                onClick = {
                    clickIntents.openProfileFragment(
                        "ahi3646",
                        "3"
                    )
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
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            ),
            HomeScreenTabConfig.Assigned(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            ),
            HomeScreenTabConfig.Mentioned(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            ),
            HomeScreenTabConfig.Participated(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            )
        )
    }

    private fun createActionTabsForPullRequests(): ImmutableList<HomeScreenTabConfig> {
        return persistentListOf(
            HomeScreenTabConfig.Created(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            ),
            HomeScreenTabConfig.Assigned(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            ),
            HomeScreenTabConfig.Mentioned(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            ),
            HomeScreenTabConfig.ReviewRequest(
                onTabChange = clickIntents::onTabChange,
                onTabStateChange = clickIntents::onTabStateChange
            )
        )
    }

}