package com.hasan.jetfasthub.screens.main.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.components.AppBar
import com.hasan.jetfasthub.screens.main.home.presentation.components.BottomNav
import com.hasan.jetfasthub.screens.main.home.presentation.components.DrawerBody
import com.hasan.jetfasthub.screens.main.home.presentation.components.DrawerHeader
import com.hasan.jetfasthub.screens.main.home.presentation.components.LogoutSheetContent
import com.hasan.jetfasthub.screens.main.home.presentation.feeds.FeedsScreen
import com.hasan.jetfasthub.screens.main.home.presentation.issues.IssuesScreen
import com.hasan.jetfasthub.screens.main.home.presentation.pull_requests.PullRequestScreen
import com.hasan.jetfasthub.screens.main.home.configs.HomeScreenPreview
import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_sheet.HomeScreenBottomSheets
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(state: HomeScreenStateConfig) {
    val sheetScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetContent = {
            state.bottomSheetConfig?.let { config ->
                if (config.isShow) {
                    when (config.content) {
                        // use HomeScreenBottomSheets for other sheets implementation
                        is HomeScreenBottomSheets.LogoutSheet -> {
                            LogoutSheetContent(
                                modifier = Modifier
                                    .background(JetHubTheme.colors.background.primary)
                                    .padding(JetHubTheme.dimens.spacing16),
                                sheetState = sheetState
                            )
                        }
                    }
                }
            }
        },
        sheetPeekHeight = JetHubTheme.dimens.size0,
        sheetShape = JetHubTheme.shapes.bottomSheet,
        sheetBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    sheetScope.launch {
                        if (sheetState.isExpanded) {
                            sheetState.collapse()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Scaffold(
            modifier = Modifier
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (sheetState.isExpanded) {
                                sheetScope.launch {
                                    sheetState.collapse()
                                }
                            }
                        }
                    )
                },
            topBar = {
                val elevation = when (state.bottomNavBarConfig.selectedBottomBarItem) {
                    AppScreens.Feeds -> JetHubTheme.dimens.elevation8
                    AppScreens.Issues, AppScreens.PullRequests -> JetHubTheme.dimens.elevation0
                }
                AppBar(
                    elevation = elevation,
                    config = state.topAppBarConfig
                )
            },
            drawerGesturesEnabled = state.drawerScreenConfig.isOpen,
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
        ) {
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
    }
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
