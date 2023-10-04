package com.hasan.jetfasthub.screens.main.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LogoutSheetContent(
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState
) {
    val sheetScope = rememberCoroutineScope()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.logout),
            style = JetHubTheme.typography.h2,
            color = JetHubTheme.colors.text.tertiary
        )
        Text(
            modifier = Modifier.padding(top = JetHubTheme.dimens.spacing8),
            text = stringResource(id = R.string.are_you_sure),
            style = JetHubTheme.typography.subtitle1,
            color = JetHubTheme.colors.text.primary1
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = JetHubTheme.dimens.spacing16),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { sheetScope.launch { sheetState.collapse() } },
                modifier = Modifier.padding(start = JetHubTheme.dimens.spacing12),
                shape = JetHubTheme.shapes.roundedCornersMedium,
                content = {
                    Text(
                        text = stringResource(id = R.string.yes),
                        style = JetHubTheme.typography.button,
                        color = JetHubTheme.colors.text.primary1
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = JetHubTheme.colors.text.disabled
                ),
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = JetHubTheme.colors.text.disabled
                ),
                modifier = Modifier.padding(start = JetHubTheme.dimens.spacing12),
                shape = JetHubTheme.shapes.roundedCornersMedium,
                onClick = { sheetScope.launch { sheetState.collapse() } },
                content = {
                    Text(
                        text = stringResource(id = R.string.no),
                        color = JetHubTheme.colors.text.primary1,
                        style = JetHubTheme.typography.button
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun LogoutSheet_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        LogoutSheetContent(
            modifier = Modifier.padding(JetHubTheme.dimens.spacing16),
            sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun LogoutSheet_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        Column(Modifier.background(JetHubTheme.colors.background.primary)) {
            LogoutSheetContent(
                modifier = Modifier.padding(JetHubTheme.dimens.spacing16),
                sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
            )
        }
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
