package com.hasan.jetfasthub.screens.main.home.presentation.state.factory

import androidx.paging.PagingData
import arrow.core.Either
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.PullRequestsStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.HomeScreenStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.BottomNavBarConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.FeedsStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.IssuesStateConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheetConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheets
import com.hasan.jetfasthub.screens.main.home.presentation.state.converters.DrawerStateConverter
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import kotlinx.coroutines.flow.Flow

class HomeScreenFactory(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
    private val clickIntents: HomeScreenIntents
) {

    private val homeScreenStateConverter by lazy {
        HomeScreenStateConverter(clickIntents = clickIntents)
    }

    private val drawerStateConverter by lazy {
        DrawerStateConverter(currentStateProvider = currentStateProvider)
    }

    private val bottomNavBarConverter by lazy {
        BottomNavBarConverter(
            currentStateProvider = currentStateProvider
        )
    }

    private val feedsStateConverter by lazy {
        FeedsStateConverter(
            currentStateProvider = currentStateProvider,
            clickIntents = clickIntents
        )
    }

    private val pullRequestStateConverter by lazy {
        PullRequestsStateConverter(currentStateProvider = currentStateProvider)
    }

    private val issuesStateConverter by lazy {
        IssuesStateConverter(currentStateProvider = currentStateProvider)
    }

    fun getStateWithLogoutBottomSheet(): HomeScreenStateConfig {
        return currentStateProvider().copy(
            bottomSheetConfig = HomeScreenBottomSheetConfig(
                isShow = true,
                onDismissRequest = clickIntents::onDismissBottomSheet,
                content = HomeScreenBottomSheets.LogoutSheet(
                    onRegret = clickIntents::onDismissBottomSheet,
                    onAccept = clickIntents::onDismissBottomSheet
                )
            )
        )
    }

    fun getStateWithClosedBottomSheet(): HomeScreenStateConfig{
        val state = currentStateProvider()
        return state.copy(
            bottomSheetConfig =state.bottomSheetConfig?.copy(isShow = false)
        )
    }

    fun updateIssuesTab(index: Int): HomeScreenStateConfig {
        return currentStateProvider().copy(
            issuesScreenConfig = issuesStateConverter.updateTabs(index)
        )
    }

    fun updatePullsTab(index: Int): HomeScreenStateConfig {
        return currentStateProvider().copy(
            pullRequestsScreenConfig = pullRequestStateConverter.updateTabs(index)
        )
    }

    fun updateDrawerTab(index: Int): HomeScreenStateConfig {
        return currentStateProvider().copy(
            drawerScreenConfig = drawerStateConverter.updateTab(index)
        )
    }

    fun updateIssueTabState(type: MyIssuesType, state: IssueState): HomeScreenStateConfig {
        return currentStateProvider().copy(
            issuesScreenConfig = issuesStateConverter.updateIssueTabState(type, state)
        )
    }

    fun updatePullsTabState(type: MyIssuesType, state: IssueState): HomeScreenStateConfig {
        return currentStateProvider().copy(
            pullRequestsScreenConfig = pullRequestStateConverter.updateIssueTabState(type, state)
        )
    }

    fun getRefreshedState(): HomeScreenStateConfig {
        return currentStateProvider().copy(
            feedsScreenConfig = feedsStateConverter.getRefreshedState(
                false
            )
        )
    }

    fun getRefreshingState(): HomeScreenStateConfig {
        return currentStateProvider().copy(
            feedsScreenConfig = feedsStateConverter.getRefreshedState(
                true
            )
        )
    }

    fun getInitialState(username: String): HomeScreenStateConfig {
        return homeScreenStateConverter.convert(username)
    }

    fun getBottomBarScreenState(screen: AppScreens): HomeScreenStateConfig {
        return currentStateProvider().copy(
            bottomNavBarConfig = bottomNavBarConverter(screen)
        )
    }

    fun getFeeds(feeds: Flow<PagingData<ReceivedEventsModel>>): HomeScreenStateConfig {
        return currentStateProvider().copy(
            feedsScreenConfig = feedsStateConverter.convert(feeds)
        )
    }

    fun getPullRequestsWithType(
        eitherIssuesModel: Either<NetworkErrors, IssuesModel>,
        type: MyIssuesType
    ): HomeScreenStateConfig {
        return currentStateProvider().copy(
            pullRequestsScreenConfig = pullRequestStateConverter(eitherIssuesModel, type)
        )
    }

    fun getIssuesWithType(
        eitherIssuesModel: Either<NetworkErrors, IssuesModel>,
        type: MyIssuesType
    ): HomeScreenStateConfig {
        return currentStateProvider().copy(
            issuesScreenConfig = issuesStateConverter(eitherIssuesModel, type)
        )
    }

    fun getUserData(eitherUserData: Either<NetworkErrors, GitHubUser>): HomeScreenStateConfig {
        return currentStateProvider().copy(
            drawerScreenConfig = drawerStateConverter.convert(eitherUserData)
        )
    }

}