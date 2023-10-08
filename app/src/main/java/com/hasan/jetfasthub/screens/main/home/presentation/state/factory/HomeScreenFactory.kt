package com.hasan.jetfasthub.screens.main.home.presentation.state.factory

import androidx.paging.PagingData
import arrow.core.Either
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.LoadedPullRequestConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.HomeScreenStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.LoadedBottomBarConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.LoadedFeedsStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.LoadedIssuesStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.AppScreens
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import kotlinx.coroutines.flow.Flow

class HomeScreenFactory(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
    private val clickIntents: HomeScreenIntents
) {

    private val skeletonStateConverter by lazy {
        HomeScreenStateConverter(clickIntents = clickIntents)
    }

    private val loadedBottomNavBarConverter by lazy {
        LoadedBottomBarConverter(
            currentStateProvider = currentStateProvider
        )
    }

    private val loadedFeedsScreenStateConverter by lazy {
        LoadedFeedsStateConverter(
            currentStateProvider = currentStateProvider,
            clickIntents = clickIntents
        )
    }

    private val loadedPullRequestStateConverter by lazy {
        LoadedPullRequestConverter(currentStateProvider = currentStateProvider)
    }

    private val loadedIssuesStateConverter by lazy {
        LoadedIssuesStateConverter(currentStateProvider = currentStateProvider)
    }

    fun changeIssuesTab(index: Int): HomeScreenStateConfig {
        return currentStateProvider().copy(
            issuesScreenConfig = loadedIssuesStateConverter.updateTabs(index)
        )
    }

    fun updateIssueTabState(index: Int, state: IssueState): HomeScreenStateConfig {
        return currentStateProvider().copy(
            issuesScreenConfig = loadedIssuesStateConverter.updateIssueTabState(index, state)
        )
    }

    fun getRefreshedState(): HomeScreenStateConfig {
        val state = currentStateProvider()
        return state.copy(
            feedsScreenConfig = loadedFeedsScreenStateConverter.getRefreshedState(
                false
            )
        )
    }

    fun getInitialState(): HomeScreenStateConfig {
        return skeletonStateConverter.convert("")
    }

    fun getBottomBarScreenState(screen: AppScreens): HomeScreenStateConfig {
        return currentStateProvider().copy(
            bottomNavBarConfig = loadedBottomNavBarConverter(screen)
        )
    }

    fun getLoadedEvents(feeds: Flow<PagingData<ReceivedEventsModel>>): HomeScreenStateConfig {
        return currentStateProvider().copy(
            feedsScreenConfig = loadedFeedsScreenStateConverter.convert(feeds)
        )
    }

    fun getPullRequests(eitherIssuesModel: Either<NetworkErrors, IssuesModel>): HomeScreenStateConfig {
        return currentStateProvider().copy(
            pullRequestsScreenConfig = loadedPullRequestStateConverter.convert(eitherIssuesModel)
        )
    }

    fun getIssues(eitherIssuesModel: Either<NetworkErrors, IssuesModel>): HomeScreenStateConfig {
        return currentStateProvider().copy(
            issuesScreenConfig = loadedIssuesStateConverter.convert(eitherIssuesModel)
        )
    }

}