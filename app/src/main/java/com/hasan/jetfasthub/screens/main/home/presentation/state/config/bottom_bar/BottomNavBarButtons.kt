package com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar

import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference

sealed class BottomNavBarButtons(val config: BottomNavBarItemConfig) {
    /** Lambda be invoked when manage button is clicked */
    abstract val onClick: (AppScreens) -> Unit

    data class Feeds(override val onClick: (screen: AppScreens) -> Unit) : BottomNavBarButtons(
        config = BottomNavBarItemConfig(
            onClick = onClick,
            title = TextReference.Res(id = R.string.feeds),
            iconResId = R.drawable.ic_github,
            screen = AppScreens.Feeds
        )
    )
    data class Issues(override val onClick: (screen: AppScreens) -> Unit) : BottomNavBarButtons(
        config = BottomNavBarItemConfig(
            onClick = onClick,
            title = TextReference.Res(id = R.string.issues),
            iconResId = R.drawable.ic_info_outline,
            screen = AppScreens.Issues
        )
    )
    data class PullRequests(override val onClick: (screen: AppScreens) -> Unit) : BottomNavBarButtons(
        config = BottomNavBarItemConfig(
            onClick = onClick,
            title = TextReference.Res(id = R.string.pull_requests),
            iconResId = R.drawable.ic_pull_requests,
            screen = AppScreens.PullRequests
        )
    )
}