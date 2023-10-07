package com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar

import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens
import kotlinx.collections.immutable.ImmutableList

data class BottomNavBarConfig(
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds,
    val buttons: ImmutableList<BottomNavBarButtons>
)
