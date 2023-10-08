package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import android.util.Log
import arrow.core.Either
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class LoadedPullRequestConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
) : Converter<Either<NetworkErrors, IssuesModel>, PullRequestsScreenConfig> {

    override fun convert(value: Either<NetworkErrors, IssuesModel>): PullRequestsScreenConfig {
        return value.fold(
            ifLeft = ::convertError,
            ifRight = ::convert
        )
    }

    private fun convertError(e: NetworkErrors): PullRequestsScreenConfig {
        Log.d("ahi3646", "convertError: $e ")
        return PullRequestsScreenConfig.Error(
            tabIndex = currentStateProvider().pullRequestsScreenConfig.tabIndex,
            actionTabs = currentStateProvider().pullRequestsScreenConfig.actionTabs)
    }

    private fun convert(issuesModel: IssuesModel): PullRequestsScreenConfig {
        return PullRequestsScreenConfig.Content(
            tabIndex = currentStateProvider().pullRequestsScreenConfig.tabIndex,
            actionTabs = currentStateProvider().pullRequestsScreenConfig.actionTabs,
            pullCreated = issuesModel,
            pullMentioned = issuesModel,
            pullAssigned = issuesModel,
            pullReviewRequest = issuesModel,
        )
    }

}