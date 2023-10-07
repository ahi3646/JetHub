package com.hasan.jetfasthub.screens.main.home.configs.converters

import android.util.Log
import arrow.core.Either
import com.hasan.jetfasthub.screens.main.home.configs.Provider
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.PullRequestsScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class LoadedPullRequestConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
) : Converter<Either<Exception, IssuesModel>, PullRequestsScreenConfig> {

    override fun convert(value: Either<Exception, IssuesModel>): PullRequestsScreenConfig {
        return value.fold(
            ifLeft = ::convertError,
            ifRight = ::convert
        )
    }

    private fun convertError(e: Exception): PullRequestsScreenConfig{
        Log.d("ahi3646", "convertError: $e ")
        val state= currentStateProvider()
        return PullRequestsScreenConfig.Error(actionTabs = state.pullRequestsScreenConfig.actionTabs)
    }

    private fun convert(issuesModel: IssuesModel): PullRequestsScreenConfig{
        return PullRequestsScreenConfig.Content(
            actionTabs = currentStateProvider().pullRequestsScreenConfig.actionTabs,
            pullCreated = issuesModel,
            pullMentioned = issuesModel,
            pullAssigned = issuesModel,
            pullReviewRequest = issuesModel,
        )
    }

}