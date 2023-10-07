package com.hasan.jetfasthub.screens.main.home.configs.state.bottom_bar

import androidx.annotation.DrawableRes
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.screens.main.home.configs.state.AppScreens

data class BottomNavBarItemConfig(
    val screen: AppScreens ,
    val onClick: (screen: AppScreens) -> Unit,
    val title: TextReference,
    @DrawableRes val iconResId: Int,
)