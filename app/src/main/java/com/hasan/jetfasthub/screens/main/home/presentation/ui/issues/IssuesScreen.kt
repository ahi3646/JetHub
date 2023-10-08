package com.hasan.jetfasthub.screens.main.home.presentation.ui.issues

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.components.EmptyScreen
import com.hasan.jetfasthub.core.ui.components.ErrorScreen
import com.hasan.jetfasthub.core.ui.components.LoadingScreen
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.res.RippleCustomTheme
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.TabItem
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

@Composable
fun IssuesScreen(
    contentPaddingValues: PaddingValues,
    config: IssuesScreenConfig,
) {
    when (config) {
        is IssuesScreenConfig.Loading -> {
            CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                ScrollableTabRow(
                    containerColor = JetHubTheme.colors.background.secondary,
                    contentColor = JetHubTheme.colors.text.primary1,
                    edgePadding = JetHubTheme.dimens.size0,
                    selectedTabIndex = config.tabIndex
                ) {
                    config.actionTabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = config.tabIndex == index,
                            onClick = {  },
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
            }
            when (config.tabIndex) {
                0,
                1,
                2,
                3 -> LoadingScreen()
            }
        }

        is IssuesScreenConfig.Error -> {
            CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                ScrollableTabRow(
                    containerColor = JetHubTheme.colors.background.secondary,
                    contentColor = JetHubTheme.colors.text.primary1,
                    edgePadding = JetHubTheme.dimens.size0,
                    selectedTabIndex = config.tabIndex
                ) {
                    config.actionTabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = config.tabIndex == index,
                            onClick = {  },
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
            }
            when (config.tabIndex) {
                0,
                1,
                2,
                3 -> ErrorScreen()
            }
        }

        is IssuesScreenConfig.Content -> {
            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(color = JetHubTheme.colors.background.primary),
                content = {
                    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                        ScrollableTabRow(
                            containerColor = JetHubTheme.colors.background.secondary,
                            contentColor = JetHubTheme.colors.text.primary1,
                            edgePadding = JetHubTheme.dimens.size0,
                            selectedTabIndex = config.tabIndex
                        ) {
                            config.actionTabs.forEachIndexed { index, tabType ->
                                Tab(
                                    selected = config.tabIndex == index,
                                    onClick = {},
                                    content = {
                                        TabItem(
                                            //fixme
                                            issuesCount = config.issuesCreated.total_count ?: 0,
                                            state = tabType.config.state,
                                            tabName = tabType.config.text,
                                            onClick = {
                                                tabType.onTabChange(index)
                                                //tabIndex = index
                                            },
                                            onStateChanged = { state ->
                                                tabType.onTabStateChange(state)
                                            },
                                        )
                                    }
                                )
                            }
                        }
                    }
                    when (config.tabIndex) {
                        0 -> IssuesScreenContent(config.issuesCreated)

                        1 -> IssuesScreenContent(config.issuesAssigned)

                        2 -> IssuesScreenContent(config.issuesMentioned)

                        3 -> IssuesScreenContent(config.issuesParticipated)
                    }
                }
            )
        }
    }
}

@Composable
fun IssuesScreenContent(issuesModel: IssuesModel) {
    if (issuesModel.items.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(issuesModel.items) { index, issue ->
                IssuesItemCard(
                    issue = issue,
                    // FIXME:
                )
                if (index < issuesModel.items.lastIndex) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                    )
                }
            }
        }
    } else {
        EmptyScreen(
            message = stringResource(id = R.string.no_issues),
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary)
        )
    }
}
