package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import androidx.paging.PagingData
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents
import kotlinx.coroutines.flow.Flow

class FeedsStateConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
    private val clickIntents: HomeScreenIntents
) : Converter<Flow<PagingData<ReceivedEventsModel>>, FeedsScreenConfig> {

    private val refreshStateConverter by lazy {
        HomeScreenPullToRefreshStateConverter(
            currentStateProvider = currentStateProvider,
            clickIntents = clickIntents
        )
    }

    fun getRefreshedState(isRefreshing: Boolean): FeedsScreenConfig {
        return refreshStateConverter.convert(isRefreshing)
    }

    override fun convert(value: Flow<PagingData<ReceivedEventsModel>>): FeedsScreenConfig {
        return currentStateProvider().feedsScreenConfig.copyFeeds(value)
    }
}