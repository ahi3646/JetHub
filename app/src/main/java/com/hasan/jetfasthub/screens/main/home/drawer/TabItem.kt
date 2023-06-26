package com.hasan.jetfasthub.screens.main.home.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem (
    val title: String,
    val screen: @Composable () -> Unit
)