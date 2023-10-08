package com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar

import com.hasan.jetfasthub.screens.main.home.presentation.state.config.AppScreens
import kotlinx.collections.immutable.ImmutableList

data class BottomNavBarConfig(
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds,
    val buttons: ImmutableList<BottomNavBarButtons>
)
