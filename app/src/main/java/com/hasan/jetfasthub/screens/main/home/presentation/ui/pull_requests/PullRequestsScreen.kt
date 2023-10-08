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
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.components.EmptyScreen
import com.hasan.jetfasthub.core.ui.components.ErrorScreen
import com.hasan.jetfasthub.core.ui.components.LoadingScreen
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.res.RippleCustomTheme
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.hasan.jetfasthub.screens.main.home.presentation.ui.components.TabItem
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

@Composable
fun PullRequestScreen(
    contentPaddingValues: PaddingValues,
    config: PullRequestsScreenConfig,
) {
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
                        val totalCount = when (tabType.config.type) {
                            MyIssuesType.CREATED -> {
                                config.pullCreated.data?.total_count
                            }

                            MyIssuesType.ASSIGNED -> {
                                config.pullAssigned.data?.total_count
                            }

                            MyIssuesType.MENTIONED -> {
                                config.pullMentioned.data?.total_count
                            }

                            MyIssuesType.PARTICIPATED, MyIssuesType.REVIEW -> {
                                config.pullReviewRequest.data?.total_count
                            }
                        }
                        Tab(
                            selected = config.tabIndex == index,
                            onClick = {},
                            content = {
                                TabItem(
                                    issuesCount = totalCount ?: 0,
                                    config = tabType.config,
                                    index = index
                                )
                            }
                        )
                    }
                }
            }
            when (config.tabIndex) {
                0 -> PullRequestScreenContent(config.pullCreated)
                1 -> PullRequestScreenContent(config.pullAssigned)
                2 -> PullRequestScreenContent(config.pullMentioned)
                3 -> PullRequestScreenContent(config.pullReviewRequest)
            }
        }
    )
}

@Composable
fun PullRequestScreenContent(pullRequestsModel: Resource<IssuesModel>){
    when(pullRequestsModel){
        is Resource.Loading -> LoadingScreen()
        is Resource.Failure -> ErrorScreen()
        is Resource.Success -> ContentScreen(
            pullRequestsModel.data!!
        )
    }
}



@Composable
private fun ContentScreen(
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