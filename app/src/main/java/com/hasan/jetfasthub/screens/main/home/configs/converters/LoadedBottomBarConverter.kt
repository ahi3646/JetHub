package com.hasan.jetfasthub.screens.main.home.configs.converters

import com.hasan.jetfasthub.screens.main.home.configs.Provider
import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar.BottomNavBarConfig

class LoadedBottomBarConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>,
) : Converter<AppScreens, BottomNavBarConfig>{

    override fun convert(value: AppScreens): BottomNavBarConfig {
        return currentStateProvider().bottomNavBarConfig.copy(selectedBottomBarItem = value)
    }

    operator fun invoke(screens: AppScreens): BottomNavBarConfig = convert(screens)


}