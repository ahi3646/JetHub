package com.hasan.jetfasthub.screens.main.home.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.AppBar
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.BottomNav
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.DrawerBody
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.DrawerHeader
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.LogoutSheet
import com.hasan.jetfasthub.screens.main.home.presentation.ui.feeds.FeedsScreen
import com.hasan.jetfasthub.screens.main.home.presentation.ui.issues.IssuesScreen
import com.hasan.jetfasthub.screens.main.home.presentation.ui.pull_requests.PullRequestScreen
import com.hasan.jetfasthub.screens.main.home.presentation.state.HomeScreenPreview
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheets
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(state: HomeScreenStateConfig) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val elevationForTopAppBar = when (state.bottomNavBarConfig.selectedBottomBarItem) {
        AppScreens.Feeds -> JetHubTheme.dimens.elevation8
        else -> JetHubTheme.dimens.elevation0
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(config = state.topAppBarConfig, elevation = elevationForTopAppBar) {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = JetHubTheme.colors.background.secondary)
                    .padding(all = JetHubTheme.dimens.spacing16),
                state = state.drawerScreenConfig.drawerHeaderConfig
            )
            DrawerBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = JetHubTheme.colors.background.secondary),
                state = state.drawerScreenConfig.drawerBodyConfig,
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        bottomBar = {
            BottomNav(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .heightIn(min = JetHubTheme.dimens.size58),
                config = state.bottomNavBarConfig,
                elevation = JetHubTheme.dimens.elevation16
            )
        },
        content = {
            Column {
                when (state.bottomNavBarConfig.selectedBottomBarItem) {
                    AppScreens.Feeds -> FeedsScreen(
                        contentPaddingValues = it,
                        state = state.feedsScreenConfig,
                    )

                    AppScreens.Issues -> IssuesScreen(
                        contentPaddingValues = it,
                        config = state.issuesScreenConfig,
                    )

                    AppScreens.PullRequests -> PullRequestScreen(
                        contentPaddingValues = it,
                        config = state.pullRequestsScreenConfig,
                    )
                }
            }
            state.bottomSheetConfig?.let { config ->
                if (config.isShow) {
                    when (config.content) {
                        // use HomeScreenBottomSheets for other sheets implementation
                        is HomeScreenBottomSheets.LogoutSheet -> {
                            LogoutSheet(
                                config = config,
                                modifier = Modifier
                                    .background(JetHubTheme.colors.background.primary)
                                    .padding(JetHubTheme.dimens.spacing16),
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun HomeScreen_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        HomeScreen(
            state = HomeScreenPreview.homeScreenPreview
        )
    }
}

@Preview
@Composable
fun HomeScreen_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        HomeScreen(
            state = HomeScreenPreview.homeScreenPreview
        )
    }
}
