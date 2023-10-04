package com.hasan.jetfasthub.screens.main.home.presentation.pull_requests

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenState
import com.hasan.jetfasthub.screens.main.home.presentation.components.TabItem
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

@Composable
fun PullRequestScreen(
    contentPaddingValues: PaddingValues,
    state: HomeScreenState,
    onNavigate: (Int, String?, String?) -> Unit,
    onPullsStateChanges: (Int, MyIssuesType, IssueState) -> Unit
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxWidth()
            .background(color = JetHubTheme.colors.background.primary)
    ) {

        ScrollableTabRow(
            edgePadding = 0.dp,
            selectedTabIndex = tabIndex
        ) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = state.pullsCreated.data?.total_count ?: 0,
                    tabIndex = tabIndex,
                    index = 0,
                    tabName = stringResource(id = R.string.created_all_caps),
                    isOpened = state.pullScreenState[0],
                    onItemClick = {
                        tabIndex = 0
                    },
                    onStateChanged = { index, state ->
                        onPullsStateChanges(index, MyIssuesType.CREATED, state)
                    },
                )
            }

            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = state.pullsAssigned.data?.total_count ?: 0,
                    tabIndex = tabIndex,
                    index = 1,
                    tabName = stringResource(id = R.string.assigned_all_caps),
                    isOpened = state.pullScreenState[1],
                    onItemClick = {
                        tabIndex = 1
                    },
                    onStateChanged = { index, state ->
                        onPullsStateChanges(index, MyIssuesType.ASSIGNED, state)
                    },
                )
            }

            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = state.pullsMentioned.data?.total_count ?: 0,
                    tabIndex = tabIndex,
                    index = 2,
                    tabName = stringResource(id = R.string.mentioned_all_caps),
                    isOpened = state.pullScreenState[2],
                    onItemClick = {
                        tabIndex = 2
                    },
                    onStateChanged = { index, state ->
                        onPullsStateChanges(index, MyIssuesType.MENTIONED, state)
                    },
                )
            }

            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = state.pullsReview.data?.total_count ?: 0,
                    tabIndex = tabIndex,
                    index = 3,
                    tabName = stringResource(id = R.string.review_requests_all_caps),
                    isOpened = state.pullScreenState[3],
                    onItemClick = {
                        tabIndex = 3
                    },
                    onStateChanged = { index, state ->
                        onPullsStateChanges(index, MyIssuesType.REVIEW, state)
                    },
                )
            }
        }

        when (tabIndex) {
            0 -> PullRequestsContent(state.pullsCreated, onNavigate)
            1 -> PullRequestsContent(state.pullsAssigned, onNavigate)
            2 -> PullRequestsContent(state.pullsMentioned, onNavigate)
            3 -> PullRequestsContent(state.pullsReview, onNavigate)
        }
    }
}

@Composable
fun PullRequestsContent(
    pullRequestsModel: Resource<IssuesModel>,
    onNavigate: (Int, String?, String?) -> Unit
){
    when (pullRequestsModel) {
        is Resource.Loading -> LoadingScreen()
        is Resource.Failure -> ErrorScreen()
        is Resource.Success -> {
            if (pullRequestsModel.data!!.items.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = JetHubTheme.colors.background.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(pullRequestsModel.data.items) { pull ->
                        PullRequestsItem(
                            pull = pull,
                            onNavigate = onNavigate
                        )
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
    }
}