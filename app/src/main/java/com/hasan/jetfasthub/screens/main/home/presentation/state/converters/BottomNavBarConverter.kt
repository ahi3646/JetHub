package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarConfig

class BottomNavBarConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
) : Converter<AppScreens, BottomNavBarConfig> {

    override fun convert(value: AppScreens): BottomNavBarConfig {
        return currentStateProvider().bottomNavBarConfig.copy(selectedBottomBarItem = value)
    }

    operator fun invoke(screens: AppScreens): BottomNavBarConfig = convert(screens)


}