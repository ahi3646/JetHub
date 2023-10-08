package com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer

import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import retrofit2.Converter

class DrawerBodyConverter(
    private val currentStateProvider: Provider<HomeScreenStateConfig>
): Converter<Int, DrawerBodyConfig> {

    override fun convert(value: Int): DrawerBodyConfig {
        return currentStateProvider().drawerScreenConfig.drawerBodyConfig.copy(tabIndex = value)
    }

}