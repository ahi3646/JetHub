package com.hasan.jetfasthub.screens.main.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    elevation: Dp = JetHubTheme.dimens.elevation0,
    onNavigationClick: () -> Unit,
    onToolbarItemCLick: (Int, String?, String?) -> Unit
) {
    //  TODO fix this ripple effect later
    //  CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = JetHubTheme.colors.background.secondary,
            elevation = elevation
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                IconButton(onClick = { onNavigationClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_24),
                        contentDescription = null,
                        tint = JetHubTheme.colors.icon.primary1
                    )
                }
                Text(
                    color = JetHubTheme.colors.text.primary1,
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = JetHubTheme.dimens.spacing10),
                    text = stringResource(id = R.string.app_name),
                    style = JetHubTheme.typography.subtitle1,
                )
                IconButton(onClick = { onToolbarItemCLick(R.id.action_homeFragment_to_notificationsFragment, null, null) }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = JetHubTheme.colors.icon.primary1
                    )
                }
                IconButton(onClick = { onToolbarItemCLick(R.id.action_homeFragment_to_searchFragment, null, null) }) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = JetHubTheme.colors.icon.primary1
                    )
                }
            }
        }
 //   }
}

@Preview
@Composable
fun AppBar_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        AppBar(
            modifier = Modifier,
            elevation = JetHubTheme.dimens.elevation0,
            onNavigationClick = { },
            onToolbarItemCLick = { _, _, _ -> }
        )
    }
}

@Preview
@Composable
fun AppBar_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        AppBar(
            modifier = Modifier,
            elevation = JetHubTheme.dimens.elevation0,
            onNavigationClick = { },
            onToolbarItemCLick = { _, _, _ -> }
        )
    }
}