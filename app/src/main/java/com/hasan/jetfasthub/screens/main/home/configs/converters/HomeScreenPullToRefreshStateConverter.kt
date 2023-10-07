package com.hasan.jetfasthub.screens.main.home.configs.converters

import com.hasan.jetfasthub.screens.main.home.configs.Provider
import com.hasan.jetfasthub.screens.main.home.configs.state.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents

class HomeScreenPullToRefreshStateConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
    private val clickIntents: HomeScreenIntents
) : Converter<Boolean, FeedsScreenConfig> {
    override fun convert(value: Boolean): FeedsScreenConfig {
        val state = currentStateProvider().feedsScreenConfig
        return state.createToPullRefresh(value)
    }
    private fun FeedsScreenConfig.createToPullRefresh(isRefreshing: Boolean): FeedsScreenConfig {
        return copy(
            feeds = this.feeds,
            pullToRefreshConfig = pullToRefreshConfig.copy(
                isRefreshing = isRefreshing,
                onRefresh = clickIntents::onRefreshSwipe
            )
        )
    }
}