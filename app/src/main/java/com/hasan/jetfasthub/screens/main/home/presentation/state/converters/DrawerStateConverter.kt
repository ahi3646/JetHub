package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

import android.util.Log
import arrow.core.Either
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.home.domain.NetworkErrors
import com.hasan.jetfasthub.screens.main.home.presentation.state.Provider
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.HomeScreenStateConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerBodyConverter
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerScreenConfig

class DrawerStateConverter(
    val currentStateProvider: Provider<HomeScreenStateConfig>
) : Converter<Either<NetworkErrors, GitHubUser>, DrawerScreenConfig> {

    private val drawerBodyConverter by lazy {
        DrawerBodyConverter(currentStateProvider)
    }

    override fun convert(value: Either<NetworkErrors, GitHubUser>): DrawerScreenConfig {
        return value.fold(
            ifLeft = { convertErrorState(it) },
            ifRight = { convert(it) }
        )
    }

    private fun convertErrorState(error: NetworkErrors): DrawerScreenConfig {
        Log.d("ahi3646", "convertErrorState: $error ")
        val header: DrawerHeaderConfig = DrawerHeaderConfig.Error
        return currentStateProvider().drawerScreenConfig.copy(drawerHeaderConfig = header)
    }

    private fun convert(user: GitHubUser): DrawerScreenConfig {
        val header: DrawerHeaderConfig = DrawerHeaderConfig.Content(user)
        return currentStateProvider().drawerScreenConfig.copy(drawerHeaderConfig = header)
    }

    fun updateTab(index: Int):DrawerScreenConfig{
        return currentStateProvider().drawerScreenConfig.copy(
            drawerBodyConfig = drawerBodyConverter.convert(index)
        )
    }

}