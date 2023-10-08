package com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer

import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

sealed class DrawerProfileConfig(val config: DrawerMenuItemConfig){
    /** Lambda be invoked when manage button is clicked */
    abstract val onClick: () -> Unit

    data class Logout(override val onClick: () -> Unit) : DrawerProfileConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.logout),
            iconResId = R.drawable.ic_logout,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class AddAccount(override val onClick: () -> Unit) : DrawerProfileConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.add_account),
            iconResId = R.drawable.ic_add,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Repositories(override val onClick: () -> Unit) : DrawerProfileConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.repositories),
            iconResId = R.drawable.ic_repo,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Starred(override val onClick: () -> Unit) : DrawerProfileConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.starred),
            iconResId = R.drawable.baseline_star_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Pinned(override val onClick: () -> Unit) : DrawerProfileConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.pinned),
            iconResId = R.drawable.baseline_bookmark_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
}
