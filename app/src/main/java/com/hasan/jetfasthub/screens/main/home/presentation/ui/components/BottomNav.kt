package com.hasan.jetfasthub.screens.main.home.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.resolveReference
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.state.HomeScreenPreview
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.BottomNavBarConfig

@Composable
fun BottomNav(
    modifier: Modifier,
    elevation: Dp = JetHubTheme.dimens.elevation16,
    config: BottomNavBarConfig
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = JetHubTheme.colors.background.secondary,
        tonalElevation = elevation
    ) {
        config.buttons.forEach { bottomBarButton ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = bottomBarButton.config.iconResId),
                        contentDescription = stringResource(id = R.string.feeds),
                        tint = JetHubTheme.colors.text.primary1
                    )
                },
                label = {
                    Text(
                        text = bottomBarButton.config.title.resolveReference(),
                        color = JetHubTheme.colors.text.primary1
                    )
                },
                selected = false,
                onClick = { bottomBarButton.onClick(bottomBarButton.config.screen) },
            )
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
            config = HomeScreenPreview.bottomNavPreview
        )
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
            config = HomeScreenPreview.bottomNavPreview
        )
    }
}
