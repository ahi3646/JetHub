package com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer

import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar.DrawerTabConfig
import kotlinx.collections.immutable.ImmutableList

data class DrawerScreenConfig(
    val drawerHeaderConfig: DrawerHeaderConfig,
    val drawerBodyConfig: DrawerBodyConfig
)

sealed class DrawerHeaderConfig {
    data object Loading : DrawerHeaderConfig()
    data object Error : DrawerHeaderConfig()
    data class Content(val user: GitHubUser) : DrawerHeaderConfig()

}

data class  DrawerBodyConfig(
    val tabIndex: Int,
    val drawerTabs: ImmutableList<DrawerTabConfig>,
    val drawerMenuConfig: ImmutableList<DrawerMenuConfig>,
    val drawerProfileConfig: ImmutableList<DrawerProfileConfig>
)