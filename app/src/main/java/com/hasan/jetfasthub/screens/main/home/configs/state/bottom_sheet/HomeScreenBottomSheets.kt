package com.hasan.jetfasthub.screens.main.home.configs.state.bottom_sheet

import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.screens.main.home.configs.state.HomeScreenBottomSheetConfigContent

//TODO
sealed class HomeScreenBottomSheets(
    open val title: TextReference,
    open val subtitle: TextReference,
    val acceptButtonConfig: ButtonConfig,
    val regretButtonConfig: ButtonConfig,
): HomeScreenBottomSheetConfigContent {
    data class ButtonConfig(
        val text: TextReference,
        val onClick: () -> Unit,
    )
    data class LogoutSheet(
        val onRegret: () -> Unit,
        val onAccept: () -> Unit
    ) : HomeScreenBottomSheets(
        title = TextReference.Res(id = R.string.logout),
        subtitle = TextReference.Res(id = R.string.are_you_sure),
        acceptButtonConfig = ButtonConfig(
            text = TextReference.Res(id = R.string.yes),
            onClick = onAccept
        ),
        regretButtonConfig = ButtonConfig(
            text = TextReference.Res(id = R.string.no),
            onClick = onRegret
        )
    )
}