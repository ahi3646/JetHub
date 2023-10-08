package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import arrow.core.Either
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class PullRequestsStateConverter(private val currentStateProvider: Provider<HomeScreenStateConfig>) {

    operator fun invoke(
        value: Either<NetworkErrors, IssuesModel>,
        type: MyIssuesType
    ): PullRequestsScreenConfig {
        return value.fold(
            ifLeft = { convertErrorWithType(it, type) },
            ifRight = { convertWithType(it, type) }
        )
    }

    private fun convertErrorWithType(
        e: NetworkErrors,
        type: MyIssuesType
    ): PullRequestsScreenConfig =
        currentStateProvider().pullRequestsScreenConfig.copyPullsOnError(e, type)


    private fun convertWithType(
        issuesModel: IssuesModel,
        type: MyIssuesType
    ): PullRequestsScreenConfig =
        currentStateProvider().pullRequestsScreenConfig.copyPulls(issuesModel, type)

    fun updateTabs(index: Int): PullRequestsScreenConfig =
        currentStateProvider().pullRequestsScreenConfig.copyTabIndex(index)

    fun updateIssueTabState(type: MyIssuesType, state: IssueState): PullRequestsScreenConfig =
        currentStateProvider().pullRequestsScreenConfig.copyTabState(type, state)

}
