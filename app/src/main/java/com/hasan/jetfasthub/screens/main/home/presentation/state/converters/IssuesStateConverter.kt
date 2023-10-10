package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import arrow.core.Either
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.IssuesScreenConfig
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class IssuesStateConverter(private val currentStateProvider: Provider<HomeScreenStateConfig>) {

    operator fun invoke(
        value: Either<NetworkErrors, IssuesModel>,
        type: MyIssuesType
    ): IssuesScreenConfig {
        return value.fold(
            ifLeft = { convertErrorWithType(errors = it, type = type) },
            ifRight = { convertWithType(value = it, type = type) }
        )
    }

    private fun convertErrorWithType(
        errors: NetworkErrors,
        type: MyIssuesType
    ): IssuesScreenConfig =
        currentStateProvider().issuesScreenConfig.copyIssuesOnError(errors, type)

    private fun convertWithType(value: IssuesModel, type: MyIssuesType): IssuesScreenConfig =
        currentStateProvider().issuesScreenConfig.copyIssues(value, type)

    fun updateTabs(index: Int): IssuesScreenConfig =
        currentStateProvider().issuesScreenConfig.copyTabIndex(index)

    fun updateIssueTabState(type: MyIssuesType, state: IssueState): IssuesScreenConfig =
        currentStateProvider().issuesScreenConfig.copyTabState(type, state)

}