package com.hasan.jetfasthub.screens.main.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.resolveReference
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheetConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheetConfigContent
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_sheet.HomeScreenBottomSheets

@Composable
fun LogoutSheet(
    config: HomeScreenBottomSheetConfig,
    modifier: Modifier = Modifier,
) {
    if (config.content is HomeScreenBottomSheets.LogoutSheet && config.isShow) {
        JetHubBottomSheet(config = config) { content ->
            LogoutSheetContent(
                content = content as HomeScreenBottomSheets.LogoutSheet,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LogoutSheetContent(
    content: HomeScreenBottomSheets.LogoutSheet,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = content.title.resolveReference(),
            style = JetHubTheme.typography.h2,
            color = JetHubTheme.colors.text.tertiary
        )
        Text(
            modifier = Modifier.padding(top = JetHubTheme.dimens.spacing8),
            text = content.subtitle.resolveReference(),
            style = JetHubTheme.typography.subtitle1,
            color = JetHubTheme.colors.text.primary1
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = JetHubTheme.dimens.spacing16),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { content.acceptButtonConfig.onClick() },
                modifier = Modifier.padding(start = JetHubTheme.dimens.spacing12),
                shape = JetHubTheme.shapes.roundedCornersMedium,
                content = {
                    Text(
                        text = content.acceptButtonConfig.text.resolveReference(),
                        style = JetHubTheme.typography.button,
                        color = JetHubTheme.colors.text.primary1
                    )
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = JetHubTheme.colors.text.disabled),
            )
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = JetHubTheme.colors.text.disabled),
                modifier = Modifier.padding(start = JetHubTheme.dimens.spacing12),
                shape = JetHubTheme.shapes.roundedCornersMedium,
                onClick = { content.regretButtonConfig.onClick() },
                content = {
                    Text(
                        text = content.regretButtonConfig.text.resolveReference(),
                        color = JetHubTheme.colors.text.primary1,
                        style = JetHubTheme.typography.button
                    )
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LogoutSheet_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        LogoutSheetContent(
            content = HomeScreenBottomSheets.LogoutSheet({}, {}),
            modifier = Modifier.padding(JetHubTheme.dimens.spacing16),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LogoutSheet_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        Column(Modifier.background(JetHubTheme.colors.background.primary)) {
            LogoutSheetContent(
                content = HomeScreenBottomSheets.LogoutSheet({}, {}),
                modifier = Modifier.padding(JetHubTheme.dimens.spacing16),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetHubBottomSheet(
    config: HomeScreenBottomSheetConfig,
    content: @Composable ColumnScope.(HomeScreenBottomSheetConfigContent) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = config.onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = JetHubTheme.colors.background.primary,
        shape = JetHubTheme.shapes.bottomSheetLarge,
        dragHandle = { JetHubBottomSheetDraggableHeader() },
    ) {
        content(config.content)
    }
}

@Composable
fun JetHubBottomSheetDraggableHeader() {
    Surface(
        modifier = Modifier
            .height(JetHubTheme.dimens.size20),
        color = JetHubTheme.colors.background.primary,
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = JetHubTheme.dimens.spacing8)
                .size(
                    width = JetHubTheme.dimens.size32,
                    height = JetHubTheme.dimens.size4,
                )
                .background(
                    color = JetHubTheme.colors.icon.inactive,
                    shape = JetHubTheme.shapes.roundedCornersSmall,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TypographyPreview() {
    JetFastHubTheme {
        Column {
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.h1
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.h2
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.h3
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.h4
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.h5
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.h6
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.caption
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.button
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.subtitle1
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.subtitle2
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.body1
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.body2
            )
            Text(
                text = stringResource(id = R.string.logout),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.overline
            )
        }
    }
}
