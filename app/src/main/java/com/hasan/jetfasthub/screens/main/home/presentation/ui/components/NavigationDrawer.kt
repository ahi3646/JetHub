package com.hasan.jetfasthub.screens.main.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.extensions.resolveReference
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.state.HomeScreenPreview
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerBodyConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerProfileConfig
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    state: DrawerHeaderConfig
) {
    when (state) {
        DrawerHeaderConfig.Loading -> {}
        DrawerHeaderConfig.Error -> {}
        is DrawerHeaderConfig.Content -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier,
            ) {
                GlideImage(
                    failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                    imageModel = { state.user.avatar_url },
                    modifier = Modifier
                        .size(JetHubTheme.dimens.size64)
                        .clip(CircleShape)
                        .border(
                            width = JetHubTheme.dimens.size2,
                            color = JetHubTheme.colors.stroke.primary,
                            shape = CircleShape
                        ),
                    imageOptions = ImageOptions(
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                )
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(start = JetHubTheme.dimens.spacing24)
                ) {
                    Text(state.user.name.toString(), color = JetHubTheme.colors.text.primary1)
                    Text(state.user.login, color = JetHubTheme.colors.text.secondary)
                }
            }
        }
    }
}

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    state: DrawerBodyConfig,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        TabRow(
            selectedTabIndex = state.tabIndex,
            backgroundColor = Color.Transparent,
            contentColor = JetHubTheme.colors.text.primary1
        ) {
            state.drawerTabs.forEachIndexed { index, tab ->
                //TODO think 2 content color maybe useless
                Tab(
                    text = {
                        if (state.tabIndex == index) {
                            Text(
                                text = tab.config.text.resolveReference(),
                                color = JetHubTheme.colors.text.primary1,
                                style = JetHubTheme.typography.button
                            )
                        } else {
                            Text(
                                text = tab.config.text.resolveReference(),
                                color = JetHubTheme.colors.text.secondary,
                                style = JetHubTheme.typography.button
                            )
                        }
                    },
                    selected = state.tabIndex == index,
                    onClick = { tab.onTabChange(index) },
                    selectedContentColor = JetHubTheme.colors.text.primary1,
                    unselectedContentColor = JetHubTheme.colors.text.tertiary,
                )
            }
        }
        when (state.tabIndex) {
            0 -> DrawerMenuScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                state = state.drawerMenuConfig,
                onNavigate = onClick
            )

            1 -> DrawerProfileScreen(
                modifier = Modifier.fillMaxSize(),
                state = state.drawerProfileConfig,
                onNavigate = onClick
            )
        }
    }
}

@Composable
fun DrawerMenuScreen(
    modifier: Modifier = Modifier,
    state: ImmutableList<DrawerMenuConfig>,
    onNavigate: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        state.forEach {
            DrawerMenuItem(
                text = it.config.title,
                onClick = {
                    it.onClick()
                    onNavigate()
                },
                icon = painterResource(id = it.config.iconResId),
            )
        }
    }
}

@Composable
fun DrawerProfileScreen(
    modifier: Modifier = Modifier,
    state: ImmutableList<DrawerProfileConfig>,
    onNavigate: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        state.forEach {
            DrawerMenuItem(
                text = it.config.title,
                onClick = {
                    it.onClick()
                    onNavigate()
                },
                icon = painterResource(id = it.config.iconResId),
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    modifier: Modifier = Modifier,
    text: TextReference,
    onClick: () -> Unit,
    icon: Painter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = JetHubTheme.colors.text.primary2),
                onClick = { onClick() }
            )
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.padding(
                start = JetHubTheme.dimens.spacing32,
                top = JetHubTheme.dimens.spacing12,
                bottom = JetHubTheme.dimens.spacing12,
            ),
            tint = JetHubTheme.colors.icon.primary1
        )
        Text(
            text = text.resolveReference(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing24),
            style = JetHubTheme.typography.button,
            color = JetHubTheme.colors.text.primary1
        )
    }
}

@Preview
@Composable
fun DrawerMenuItemPreview() {
    JetFastHubTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(JetHubTheme.colors.background.plain)
        ) {
            DrawerMenuItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = JetHubTheme.dimens.spacing2,
                        bottom = JetHubTheme.dimens.spacing2
                    ),
                text = TextReference.Res(id = R.string.profile),
                onClick = { },
                icon = painterResource(id = R.drawable.ic_profile)
            )
        }
    }
}


@Preview
@Composable
fun NavigationDrawer_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        Column {
            DrawerBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JetHubTheme.colors.background.plain),
                state = DrawerBodyConfig(
                    tabIndex = 0,
                    drawerTabs = HomeScreenPreview.drawerTabs,
                    drawerMenuConfig = HomeScreenPreview.drawerMenuPreview,
                    drawerProfileConfig = HomeScreenPreview.drawerProfilePreview
                ),
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun NavigationDrawer_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        Column {
            DrawerBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JetHubTheme.colors.background.plain),
                state = DrawerBodyConfig(
                    tabIndex = 0,
                    drawerTabs = HomeScreenPreview.drawerTabs,
                    drawerMenuConfig = HomeScreenPreview.drawerMenuPreview,
                    drawerProfileConfig = HomeScreenPreview.drawerProfilePreview
                ),
                onClick = {}
            )
        }
    }
}

