package com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer

import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

sealed class DrawerMenuConfig(val config: DrawerMenuItemConfig) {
    /** Lambda be invoked when manage button is clicked */
    abstract val onClick: () -> Unit

    data class Profile(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.profile),
            iconResId = R.drawable.baseline_person_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Organizations(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.organizations),
            iconResId = R.drawable.baseline_people_alt_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Notifications(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.notifications),
            iconResId = R.drawable.baseline_notifications_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Pinned(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.pinned),
            iconResId = R.drawable.baseline_bookmark_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Trending(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.trending),
            iconResId = R.drawable.baseline_trending_up_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Gists(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.gists),
            iconResId = R.drawable.baseline_code_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class JetHub(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.app_name),
            iconResId = R.drawable.ic_fasthub_mascot,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Faq(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.home),
            iconResId = R.drawable.baseline_info_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class Settings(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.settings),
            iconResId = R.drawable.baseline_settings_24,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
    data class About(override val onClick: () -> Unit) : DrawerMenuConfig(
        config = DrawerMenuItemConfig(
            title = TextReference.Res(id = R.string.about),
            iconResId = R.drawable.ic_info_outline,
            textColorProvider = { JetHubTheme.colors.text.primary1 },
            onClick = onClick
        )
    )
}

