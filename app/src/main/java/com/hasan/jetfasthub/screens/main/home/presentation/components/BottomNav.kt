package com.hasan.jetfasthub.screens.main.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.AppScreens

@Composable
fun BottomNav(
    modifier: Modifier,
    onBottomBarItemSelected: (AppScreens) -> Unit,
    elevation: Dp = JetHubTheme.dimens.elevation16,
) {
    Surface(elevation = elevation) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomAppBar(containerColor = JetHubTheme.colors.background.secondary) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_github),
                            contentDescription = stringResource(id = R.string.feeds),
                            tint = JetHubTheme.colors.text.primary1
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.feeds),
                            color = JetHubTheme.colors.text.primary1
                        )
                    },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.Feeds) },
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                            contentDescription = stringResource(id = R.string.issues),
                            tint = JetHubTheme.colors.text.primary1
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.issues),
                            color = JetHubTheme.colors.text.primary1
                        )
                    },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.Issues) },
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                            contentDescription = stringResource(id = R.string.pull_requests),
                            tint = JetHubTheme.colors.text.primary1
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.pull_requests),
                            color = JetHubTheme.colors.text.primary1
                        )
                    },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.PullRequests) },
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomNav_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        BottomNav(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(58.dp),
            elevation = 16.dp,
            onBottomBarItemSelected = {})
    }
}

@Preview(locale = "en")
@Composable
fun BottomNav_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        BottomNav(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(58.dp),
            elevation = 16.dp,
            onBottomBarItemSelected = {})
    }
}
