package com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hasan.jetfasthub.core.ui.extensions.TextReference

//TODO remove textColorProvider
class DrawerMenuItemConfig(
    val title : TextReference,
    @DrawableRes val iconResId: Int,
    val textColorProvider: @Composable () -> Color,
    val onClick: () -> Unit
)