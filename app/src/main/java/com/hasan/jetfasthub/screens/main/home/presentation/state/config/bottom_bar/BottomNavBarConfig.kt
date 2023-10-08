package com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar

import kotlinx.collections.immutable.ImmutableList

data class BottomNavBarConfig(
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds,
    val buttons: ImmutableList<BottomNavBarButtons>
)

sealed interface AppScreens {
    data object Feeds : AppScreens
    data object Issues : AppScreens
    data object PullRequests : AppScreens
}
