package com.hasan.jetfasthub.screens.main.home.configs.converters

import android.util.Log
import arrow.core.Either
import com.hasan.jetfasthub.screens.main.home.configs.Provider
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class LoadedIssuesStateConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
): Converter<Either<Exception, IssuesModel>, IssuesScreenConfig> {

    override fun convert(value: Either<Exception, IssuesModel>): IssuesScreenConfig {
        return value.fold(
            ifLeft =::convertError ,
            ifRight = ::convert
        )
    }

    fun updateTabs(index: Int): IssuesScreenConfig{
        return currentStateProvider().issuesScreenConfig.copyTabIndex(index)
    }

//    fun updateTabState(state: IssueState):IssuesScreenConfig{
//        return currentStateProvider().issuesScreenConfig.copyTabState(state = state)
//    }

    private fun convertError(e: Exception): IssuesScreenConfig{
        Log.d("ahi3646", "convertError: $e ")
        val state = currentStateProvider()
        return IssuesScreenConfig.Error(
            tabIndex = state.issuesScreenConfig.tabIndex,
            actionTabs = state.issuesScreenConfig.actionTabs)
    }

    private fun convert(issues: IssuesModel): IssuesScreenConfig{
        return IssuesScreenConfig.Content(
            tabIndex = currentStateProvider().issuesScreenConfig.tabIndex,
            actionTabs = currentStateProvider().issuesScreenConfig.actionTabs,
            issuesCreated = issues,
            issuesAssigned = issues,
            issuesMentioned = issues,
            issuesParticipated = issues
        )
    }

}