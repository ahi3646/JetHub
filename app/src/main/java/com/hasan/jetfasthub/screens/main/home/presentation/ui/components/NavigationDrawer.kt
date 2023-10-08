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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.extensions.resolveReference
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.DrawerBodyConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.DrawerHeaderConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.HomeScreenPreview
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerMenuConfig
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.drawer.DrawerProfileConfig
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
    //intentReducer: (HomeScreenClickIntents) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        TabRow(
            selectedTabIndex = state.tabIndex,
            backgroundColor = Color.Transparent,
            contentColor = JetHubTheme.colors.text.primary1
        ) {
            state.drawerTabs.forEachIndexed { index, title ->
                //TODO think 2 content color maybe useless
                Tab(
                    text = {
                        if (state.tabIndex == index) {
                            Text(
                                text = stringResource(id = title),
                                color = JetHubTheme.colors.text.primary1,
                                style = JetHubTheme.typography.button
                            )
                        } else {
                            Text(
                                text = stringResource(id = title),
                                color = JetHubTheme.colors.text.secondary,
                                style = JetHubTheme.typography.button
                            )
                        }
                    },
                    selected = state.tabIndex == index,
                    onClick = { state.tabIndex = index },
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
                //intentReducer = intentReducer
            )

            1 -> DrawerProfileScreen(
                modifier = Modifier.fillMaxSize(),
                state = state.drawerProfileConfig,
                //intentReducer = intentReducer
            )
        }
    }
}

@Composable
fun DrawerMenuScreen(
    modifier: Modifier = Modifier,
    state: ImmutableList<DrawerMenuConfig>,
    //intentReducer: (HomeScreenClickIntents) -> Unit,
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
//                    intentReducer(HomeScreenClickIntents.OnDrawerClick)
                    it.onClick
                },
                icon = painterResource(id = it.config.iconResId),
            )
        }
    }

    /**
    when (state) {
    //        is DrawerState.Success -> {
    //            Column(
    //                modifier = modifier,
    //                horizontalAlignment = Alignment.Start,
    //                verticalArrangement = Arrangement.Top
    //            ) {
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.home),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OnDrawerClick) },
    //                    icon = painterResource(id = R.drawable.baseline_home_24),
    //                )
    //                Divider()
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.profile),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OpenProfileFragment(username = state.user.login)) },
    //                    icon = painterResource(id = R.drawable.baseline_person_24),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.organizations),
    //                    onClick = { },
    //                    icon = painterResource(id = R.drawable.baseline_people_alt_24),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.notifications),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OpenNotificationFragment) },
    //                    icon = painterResource(id = R.drawable.baseline_notifications_24),
    //                )
    //                Divider()
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.pinned),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OpenPinnedFragment) },
    //                    icon = painterResource(id = R.drawable.baseline_bookmark_24),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.trending),
    //                    onClick = { },
    //                    icon = painterResource(id = R.drawable.baseline_trending_up_24),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.gists),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OpenGistsFragment(state.user.login)) },
    //                    icon = painterResource(id = R.drawable.baseline_code_24),
    //                )
    //                Divider()
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.app_name),
    //                    onClick = {
    //                        intentReducer(
    //                            HomeScreenClickIntents.OpenRepositoryFragment(
    //                                Constants.JetHubOwner,
    //                                Constants.JetHubRepoName
    //                            )
    //                        )
    //                    },
    //                    icon = painterResource(id = R.drawable.ic_fasthub_mascot),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.faq),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OpenFaqFragment) },
    //                    icon = painterResource(id = R.drawable.baseline_info_24),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.settings),
    //                    onClick = { HomeScreenClickIntents.OpenSettingsFragment },
    //                    icon = painterResource(id = R.drawable.baseline_settings_24),
    //                )
    //                DrawerMenuItem(
    //                    text = stringResource(id = R.string.about),
    //                    onClick = { intentReducer(HomeScreenClickIntents.OpenAboutFragment) },
    //                    icon = painterResource(id = R.drawable.baseline_info_24),
    //                )
    //            }
    //        }
    }
     */
}

@Composable
fun DrawerProfileScreen(
    modifier: Modifier = Modifier,
    state: ImmutableList<DrawerProfileConfig>,
//    intentReducer: (HomeScreenClickIntents) -> Unit
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
//                    intentReducer(HomeScreenClickIntents.OnDrawerClick)
                    it.onClick
                },
                icon = painterResource(id = it.config.iconResId),
            )
        }
    }
//    when (state) {
//        DrawerState.Loading,
//        DrawerState.Error -> {
//            Column(
//                modifier = modifier,
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Top,
//                content = {
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.logout),
//                        onClick = { },
//                        icon = painterResource(id = R.drawable.ic_logout),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.add_account),
//                        onClick = { },
//                        icon = painterResource(id = R.drawable.ic_add),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.repositories),
//                        onClick = { },
//                        icon = painterResource(id = R.drawable.baseline_book_24),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.starred),
//                        onClick = { },
//                        icon = painterResource(id = R.drawable.baseline_star_24),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.pinned),
//                        onClick = { },
//                        icon = painterResource(id = R.drawable.baseline_bookmark_border_24),
//                    )
//                }
//            )
//        }
//
//        is DrawerState.Success -> {
//            Column(
//                modifier = modifier,
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Top,
//                content = {
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.logout),
//                        onClick = { HomeScreenClickIntents.OnLogoutClick },
//                        icon = painterResource(id = R.drawable.ic_logout),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.add_account),
//                        onClick = { },
//                        icon = painterResource(id = R.drawable.ic_add),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.repositories),
//                        onClick = {
//                            intentReducer(
//                                HomeScreenClickIntents.OpenProfileFragment(
//                                    username = state.user.login,
//                                    profileTabStartIndex = "2"
//                                )
//                            )
//                        },
//                        icon = painterResource(id = R.drawable.baseline_book_24),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.starred),
//                        onClick = {
//                            intentReducer(
//                                HomeScreenClickIntents.OpenProfileFragment(
//                                    username = state.user.login,
//                                    profileTabStartIndex = "3"
//                                )
//                            )
//                        },
//                        icon = painterResource(id = R.drawable.baseline_star_24),
//                    )
//                    Divider()
//                    DrawerMenuItem(
//                        text = stringResource(id = R.string.pinned),
//                        onClick = { intentReducer(HomeScreenClickIntents.OpenPinnedFragment) },
//                        icon = painterResource(id = R.drawable.baseline_bookmark_border_24),
//                    )
//                }
//            )
//        }
//    }
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
//            DrawerHeader(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surface)
//                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
//            )
            
            DrawerBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JetHubTheme.colors.background.plain),
                state = DrawerBodyConfig(
                    drawerMenuConfig = HomeScreenPreview.drawerMenuPreview,
                    drawerProfileConfig = HomeScreenPreview.drawerProfilePreview
                ),
//                intentReducer = { _ -> }
            )
        }
    }
}

@Preview
@Composable
fun NavigationDrawer_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        Column {
//            DrawerHeader(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(JetHubTheme.colors.background.secondary)
//                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
//            )
            val drawerMenuPreview = persistentListOf(
                DrawerMenuConfig.Home(onClick = {}),
                DrawerMenuConfig.Profile(onClick = {}),
                DrawerMenuConfig.Organizations(onClick = {}),
                DrawerMenuConfig.Notifications(onClick = {}),
                DrawerMenuConfig.Pinned(onClick = {}),
                DrawerMenuConfig.Trending(onClick = {}),
                DrawerMenuConfig.Gists(onClick = {}),
                DrawerMenuConfig.JetHub(onClick = {}),
                DrawerMenuConfig.Faq(onClick = {}),
                DrawerMenuConfig.Settings(onClick = {}),
                DrawerMenuConfig.About(onClick = {}),
            )
            val drawerProfilePreview = persistentListOf(
                DrawerProfileConfig.Logout(onClick = {}),
                DrawerProfileConfig.AddAccount(onClick = {}),
                DrawerProfileConfig.Repositories(onClick = {}),
                DrawerProfileConfig.Starred(onClick = {}),
                DrawerProfileConfig.Pinned(onClick = {}),
                )
            DrawerBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JetHubTheme.colors.background.plain),
                state = DrawerBodyConfig(
                    drawerMenuConfig = drawerMenuPreview,
                    drawerProfileConfig =drawerProfilePreview
                ),
//                intentReducer = { _ -> }
            )
        }
    }
}

