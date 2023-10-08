package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import android.util.Log
import arrow.core.Either
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class LoadedIssuesStateConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
) : Converter<Either<NetworkErrors, IssuesModel>, IssuesScreenConfig> {

    override fun convert(value: Either<NetworkErrors, IssuesModel>): IssuesScreenConfig {
        return value.fold(
            ifLeft = ::convertError,
            ifRight = ::convert
        )
    }

    fun updateTabs(index: Int): IssuesScreenConfig {
        return currentStateProvider().issuesScreenConfig.copyTabIndex(index)
    }

    fun updateIssueTabState(tabIndex: Int, state: IssueState): IssuesScreenConfig {
        return currentStateProvider().issuesScreenConfig.copyTabState(tabIndex, state)
    }

    private fun convertError(e: NetworkErrors): IssuesScreenConfig {
        Log.d("ahi3646", "convertError: $e ")
        val state = currentStateProvider()
        return IssuesScreenConfig.Error(
            tabIndex = state.issuesScreenConfig.tabIndex,
            actionTabs = state.issuesScreenConfig.actionTabs
        )
    }

//    fun convertWithType(
//        value: Either<Exception, IssuesModel>,
//        type: MyIssuesType
//    ): IssuesScreenConfig {
//        return value.fold(
//            ifLeft = {
//                convertError(it)
//            },
//            ifRight = {
//
//                IssuesScreenConfig.Con.copyIssues(it, type)
//            }
//        )
//    }

    private fun convert(issues: IssuesModel): IssuesScreenConfig {
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