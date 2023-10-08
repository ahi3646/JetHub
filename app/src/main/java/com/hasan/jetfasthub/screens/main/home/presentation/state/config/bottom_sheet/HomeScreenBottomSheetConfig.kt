package com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet

/**
 * JetHub bottom sheet config
 *
 * @property isShow           flag that determine if bottom sheet is shown
 * @property onDismissRequest lambda be invoked when bottom sheet is dismissed
 * @property content          content config
 */
data class HomeScreenBottomSheetConfig(
    val isShow: Boolean,
    val onDismissRequest: () -> Unit,
    val content: HomeScreenBottomSheetConfigContent,
)

interface HomeScreenBottomSheetConfigContent