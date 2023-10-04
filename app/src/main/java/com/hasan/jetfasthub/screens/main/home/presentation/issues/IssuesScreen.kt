package com.hasan.jetfasthub.screens.main.home.presentation.issues

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenState
import com.hasan.jetfasthub.screens.main.home.presentation.components.TabItem
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

@Composable
fun IssuesScreen(
    state: HomeScreenState,
    contentPaddingValues: PaddingValues,
    onIssueItemClicked: (Int, String, String, String) -> Unit,
    onIssuesStateChanged: (Int, MyIssuesType, IssueState) -> Unit
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxWidth()
            .background(color = JetHubTheme.colors.background.primary),
        content = {
            CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                ScrollableTabRow(
                    containerColor = JetHubTheme.colors.background.primary,
                    contentColor = JetHubTheme.colors.text.primary1,
                    edgePadding = JetHubTheme.dimens.size0,
                    selectedTabIndex = tabIndex
                ) {
                    Tab(
                        selected = tabIndex == 0,
                        onClick = { tabIndex = 0 }
                    ) {
                        TabItem(
                            issuesCount = state.issuesCreated.data?.total_count ?: 0,
                            tabIndex = tabIndex,
                            index = 0,
                            isOpened = state.issueScreenState[0],
                            tabName = stringResource(id = R.string.created_all_caps),
                            onItemClick = { tabIndex = 0 },
                            onStateChanged = { index, state ->
                                onIssuesStateChanged(index, MyIssuesType.CREATED, state)
                            },
                        )
                    }

                    Tab(
                        selected = tabIndex == 0,
                        onClick = { tabIndex = 0 }
                    ) {
                        TabItem(
                            issuesCount = state.issuesAssigned.data?.total_count ?: 0,
                            tabIndex = tabIndex,
                            index = 1,
                            isOpened = state.issueScreenState[1],
                            tabName = stringResource(id = R.string.assigned_all_caps),
                            onItemClick = {
                                tabIndex = 1
                            },
                            onStateChanged = { index, state ->
                                onIssuesStateChanged(index, MyIssuesType.ASSIGNED, state)
                            },
                        )
                    }

                    Tab(
                        selected = tabIndex == 0,
                        onClick = { tabIndex = 0 }
                    ) {
                        TabItem(
                            issuesCount = state.issuesMentioned.data?.total_count ?: 0,
                            tabIndex = tabIndex,
                            index = 2,
                            isOpened = state.issueScreenState[2],
                            tabName = stringResource(id = R.string.mentioned_all_caps),
                            onItemClick = {
                                tabIndex = 2
                            },
                            onStateChanged = { index, state ->
                                onIssuesStateChanged(index, MyIssuesType.MENTIONED, state)
                            },
                        )
                    }

                    Tab(
                        selected = tabIndex == 0,
                        onClick = { tabIndex = 0 }
                    ) {
                        TabItem(
                            issuesCount = state.issuesParticipated.data?.total_count ?: 0,
                            tabIndex = tabIndex,
                            index = 3,
                            isOpened = state.issueScreenState[3],
                            tabName = stringResource(id = R.string.participated_all_caps),
                            onItemClick = {
                                tabIndex = 3
                            },
                            onStateChanged = { index, state ->
                                onIssuesStateChanged(index, MyIssuesType.PARTICIPATED, state)
                            },
                        )
                    }

                }
            }
            when (tabIndex) {
                0 -> IssuesScreenContent(state.issuesCreated, onIssueItemClicked)
                1 -> IssuesScreenContent(state.issuesAssigned, onIssueItemClicked)
                2 -> IssuesScreenContent(state.issuesMentioned, onIssueItemClicked)
                3 -> IssuesScreenContent(state.issuesParticipated, onIssueItemClicked)
            }
        }
    )
}

@Composable
fun IssuesScreenContent(
    issuesModel: Resource<IssuesModel>,
    onIssuesItemClicked: (Int, String, String, String) -> Unit
) {
    when(issuesModel){
        is Resource.Loading -> LoadingScreen()
        is Resource.Failure -> ErrorScreen()
        is Resource.Success -> {
            if (issuesModel.data!!.items.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = JetHubTheme.colors.background.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    itemsIndexed(issuesModel.data.items) {index,  issue ->
                        IssuesItemCard(
                            issue = issue,
                            onIssuesItemClicked = onIssuesItemClicked
                        )
                        if (index < issuesModel.data.items.lastIndex) {
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
    }
}
