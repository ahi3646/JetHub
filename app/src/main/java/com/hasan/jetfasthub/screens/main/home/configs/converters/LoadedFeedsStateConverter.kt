package com.hasan.jetfasthub.screens.main.home.configs.converters

import androidx.paging.PagingData
import com.hasan.jetfasthub.screens.main.home.configs.Provider
import com.hasan.jetfasthub.screens.main.home.configs.state.FeedsScreenConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenPullToRefreshConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.hasan.jetfasthub.screens.main.home.presentation.HomeScreenIntents
import kotlinx.coroutines.flow.Flow

class LoadedFeedsStateConverter(
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
        return FeedsScreenConfig(
            feeds = value,
            pullToRefreshConfig = HomeScreenPullToRefreshConfig(
                false,
                onRefresh = clickIntents::onRefreshSwipe
            )
        )
    }

//    override fun convert(value: Either<Exception, Flow<PagingData<ReceivedEventsModel>>>): FeedsScreenConfig {
//        value.fold(
//            ifLeft = {
//                return FeedsScreenConfig(
//                    feeds = value,
//                    pullToRefreshConfig = HomeScreenPullToRefreshConfig(
//                        false,
//                        onRefresh = clickIntents::onRefreshSwipe
//                    )
//                )
//            },
//            ifRight = {feeds ->
//                return FeedsScreenConfig(
//                feeds = feeds,
//                pullToRefreshConfig = HomeScreenPullToRefreshConfig(
//                    false,
//                    onRefresh = clickIntents::onRefreshSwipe
//                )
//            ) }
//        )
//    }
}