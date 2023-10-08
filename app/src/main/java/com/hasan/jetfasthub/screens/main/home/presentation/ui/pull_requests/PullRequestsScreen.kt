package com.hasan.jetfasthub.screens.main.home.presentation.ui.pull_requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.components.EmptyScreen
import com.hasan.jetfasthub.core.ui.components.ErrorScreen
import com.hasan.jetfasthub.core.ui.components.LoadingScreen
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.TabItem
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

@Composable
fun PullRequestScreen(
    contentPaddingValues: PaddingValues,
    config: PullRequestsScreenConfig,
) {
    when (config) {
        is PullRequestsScreenConfig.Loading -> {
            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(color = JetHubTheme.colors.background.primary),
                content = {
                    ScrollableTabRow(
                        containerColor = JetHubTheme.colors.background.secondary,
                        contentColor = JetHubTheme.colors.text.primary1,
                        edgePadding = 0.dp,
                        selectedTabIndex = config.tabIndex
                    ) {
                        config.actionTabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = config.tabIndex == index,
                                onClick = { config.tabIndex = index },
                                content = {
                                    TabItem(
                                        issuesCount = 0,
                                        state = IssueState.Open,
                                        tabName = tab.config.text,
                                        onClick = {},
                                        onStateChanged = { _ -> },
                                    )
                                }
                            )
                        }
                    }
                    when (config.tabIndex) {
                        0,
                        1,
                        2,
                        3 -> LoadingScreen()
                    }
                }
            )
        }

        is PullRequestsScreenConfig.Error -> {
            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(color = JetHubTheme.colors.background.primary),
                content = {
                    ScrollableTabRow(
                        containerColor = JetHubTheme.colors.background.secondary,
                        contentColor = JetHubTheme.colors.text.primary1,
                        edgePadding = 0.dp,
                        selectedTabIndex = config.tabIndex
                    ) {
                        config.actionTabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = config.tabIndex == index,
                                onClick = { config.tabIndex = index },
                                content = {
                                    TabItem(
                                        issuesCount = 0,
                                        state = IssueState.Open,
                                        tabName = tab.config.text,
                                        onClick = {},
                                        onStateChanged = { _ -> },
                                    )
                                }
                            )
                        }
                    }
                    when (config.tabIndex) {
                        0,
                        1,
                        2,
                        3 -> ErrorScreen()
                    }
                }
            )
        }

        is PullRequestsScreenConfig.Content -> {
            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(color = JetHubTheme.colors.background.primary)
            ) {
                ScrollableTabRow(
                    containerColor = JetHubTheme.colors.background.secondary,
                    contentColor = JetHubTheme.colors.text.primary1,
                    edgePadding = 0.dp,
                    selectedTabIndex = config.tabIndex
                ) {
                    config.actionTabs.forEachIndexed { index, tabType ->
                        Tab(
                            selected = config.tabIndex == index,
                            onClick = { config.tabIndex = index },
                            selectedContentColor = JetHubTheme.colors.text.primary1,
                            unselectedContentColor = JetHubTheme.colors.text.secondary,
                            content = {
                                TabItem(
                                    issuesCount = config.pullCreated.total_count ?: 0,
                                    state = tabType.config.state,
                                    tabName = tabType.config.text,
                                    onClick = { tabType.onTabChange(index) },
                                    onStateChanged = { state -> tabType.onTabStateChange(state) },
                                )
                            }
                        )
                    }
                }
                when (config.tabIndex) {
                    0 -> PullRequestsContent(config.pullCreated)
                    1 -> PullRequestsContent(config.pullAssigned)
                    2 -> PullRequestsContent(config.pullMentioned)
                    3 -> PullRequestsContent(config.pullReviewRequest)
                }
            }
        }
    }
}

@Composable
fun PullRequestsContent(
    pullRequestsModel: IssuesModel,
) {
    if (pullRequestsModel.items.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(pullRequestsModel.items) { pull ->
                PullRequestsItem(pull = pull)
            }
        }
    } else {
        EmptyScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary),
            message = stringResource(id = R.string.no_pull)
        )
    }
}