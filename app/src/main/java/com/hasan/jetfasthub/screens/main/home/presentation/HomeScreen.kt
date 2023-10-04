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
import androidx.compose.material.ScaffoldState
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.home.presentation.components.AppBar
import com.hasan.jetfasthub.screens.main.home.presentation.components.BottomNav
import com.hasan.jetfasthub.screens.main.home.presentation.components.DrawerBody
import com.hasan.jetfasthub.screens.main.home.presentation.components.DrawerHeader
import com.hasan.jetfasthub.screens.main.home.presentation.components.LogoutSheetContent
import com.hasan.jetfasthub.screens.main.home.presentation.feeds.FeedsScreen
import com.hasan.jetfasthub.screens.main.home.presentation.issues.IssuesScreen
import com.hasan.jetfasthub.screens.main.home.presentation.pull_requests.PullRequestScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
    onBottomBarItemSelected: (AppScreens) -> Unit,
    onNavigate: (Int, String?, String?) -> Unit,
    onIssueItemClicked: (Int, String, String, String) -> Unit,
    onIssuesStateChanged: (Int, MyIssuesType, IssueState) -> Unit,
    onPullsStateChanged: (Int, MyIssuesType, IssueState) -> Unit,
    scaffoldState: ScaffoldState,
    onNavigationClick: () -> Unit
) {
    val sheetScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetContent = {
            LogoutSheetContent(
                modifier = Modifier
                    .background(JetHubTheme.colors.background.primary)
                    .padding(JetHubTheme.dimens.spacing16),
                sheetState = sheetState
            )
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
            scaffoldState = scaffoldState,
            topBar = {
                AppBar(
                    onNavigationClick = onNavigationClick,
                    onToolbarItemCLick = onNavigate
                )
            },
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            drawerContent = {
                DrawerHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = JetHubTheme.colors.background.secondary)
                        .padding(all = JetHubTheme.dimens.spacing16),
                    user = state.user
                )
                DrawerBody(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = JetHubTheme.colors.background.secondary),
                    closeDrawer = onNavigationClick,
                    username = state.user.data?.login ?: "",
                    onLogout = {
                        onNavigationClick()
                        sheetScope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    },
                    onNavigate = onNavigate
                )
            },
            bottomBar = {
                BottomNav(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .heightIn(min = JetHubTheme.dimens.size58),
                    onBottomBarItemSelected = onBottomBarItemSelected,
                )
            },
        ) {
            when (state.selectedBottomBarItem) {
                AppScreens.Feeds -> FeedsScreen(
                    contentPaddingValues = it,
                    isRefreshing = isRefreshing,
                    pullRefreshState = pullRefreshState,
                    onNavigate = onNavigate,
                    events = state.events.collectAsLazyPagingItems()
                )

                AppScreens.Issues -> IssuesScreen(
                    contentPaddingValues = it,
                    state = state,
                    onIssueItemClicked = onIssueItemClicked,
                    onIssuesStateChanged = onIssuesStateChanged
                )

                AppScreens.PullRequests -> PullRequestScreen(
                    contentPaddingValues = it,
                    state = state,
                    onNavigate = onNavigate,
                    onPullsStateChanges = onPullsStateChanged
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun HomeScreen_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        HomeScreen(
            state = HomeScreenState(),
            pullRefreshState = rememberPullRefreshState(
                refreshing = false,
                onRefresh = { /*TODO*/ }),
            isRefreshing = false,
            onBottomBarItemSelected = {},
            onNavigate = { _, _, _ -> },
            onIssueItemClicked = { _, _, _, _ -> },
            onIssuesStateChanged = { _, _, _ -> },
            onPullsStateChanged = { _, _, _ -> },
            scaffoldState = rememberScaffoldState(),
            onNavigationClick = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun HomeScreen_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        HomeScreen(
            state = HomeScreenState(),
            pullRefreshState = rememberPullRefreshState(
                refreshing = false,
                onRefresh = { /*TODO*/ }),
            isRefreshing = false,
            onBottomBarItemSelected = {},
            onNavigate = { _, _, _ -> },
            onIssueItemClicked = { _, _, _, _ -> },
            onIssuesStateChanged = { _, _, _ -> },
            onPullsStateChanged = { _, _, _ -> },
            scaffoldState = rememberScaffoldState(),
            onNavigationClick = {}
        )
    }
}
