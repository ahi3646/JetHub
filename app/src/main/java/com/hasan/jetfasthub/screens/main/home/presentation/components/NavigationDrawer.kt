package com.hasan.jetfasthub.screens.main.home.presentation.components

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
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    user: Resource<GitHubUser>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier,
    ) {
        GlideImage(
            failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
            imageModel = { user.data?.avatar_url },
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
            Text(user.data?.name.toString(), color = JetHubTheme.colors.text.primary1)
            Text(user.data?.login.toString(), color = JetHubTheme.colors.text.secondary)
        }
    }
}

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit,
    username: String,
    onLogout: () -> Unit,
    onNavigate: (Int, String?, String?) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("MENU", "PROFILE")
    Column(
        modifier = modifier,
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = Color.Transparent,
            contentColor = JetHubTheme.colors.text.primary1
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        if (tabIndex == index) {
                            Text(
                                text = title,
                                color = JetHubTheme.colors.text.primary1,
                                style = JetHubTheme.typography.button
                            )
                        } else {
                            Text(
                                text = title,
                                color = JetHubTheme.colors.text.secondary,
                                style = JetHubTheme.typography.button
                            )
                        }
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = JetHubTheme.colors.text.primary1,
                    unselectedContentColor = JetHubTheme.colors.text.tertiary,
                )
            }
        }
        when (tabIndex) {
            0 -> DrawerMenuScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                closeDrawer = closeDrawer,
                username = username,
                onNavigate = onNavigate
            )

            1 -> DrawerProfileScreen(
                modifier = Modifier.fillMaxSize(),
                username = username,
                onNavigate = onNavigate,
                onLogout = onLogout
            )
        }
    }
}

@Composable
fun DrawerMenuScreen(
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit,
    username: String,
    onNavigate: (Int, String?, String?) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        DrawerMenuItem(
            text = stringResource(id = R.string.home),
            onClick = { closeDrawer() },
            icon = painterResource(id = R.drawable.baseline_home_24),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.profile),
            onClick = { onNavigate(R.id.action_homeFragment_to_profileFragment, username, null) },
            icon = painterResource(id = R.drawable.baseline_person_24),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.organizations),
            onClick = { },
            icon = painterResource(id = R.drawable.baseline_people_alt_24),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.notifications),
            onClick = { onNavigate(R.id.action_homeFragment_to_notificationsFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_notifications_24),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.pinned),
            onClick = { onNavigate(R.id.action_homeFragment_to_pinnedFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_bookmark_24),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.trending),
            onClick = { onNavigate(R.id.action_homeFragment_to_notificationsFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_trending_up_24),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.gists),
            onClick = { onNavigate(R.id.action_homeFragment_to_gistsFragment, username, null) },
            icon = painterResource(id = R.drawable.baseline_code_24),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.app_name),
            onClick = {
                onNavigate(
                    R.id.action_homeFragment_to_repositoryFragment,
                    Constants.JetHubOwner,
                    Constants.JetHubRepoName
                )
            },
            icon = painterResource(id = R.drawable.ic_fasthub_mascot),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.faq),
            onClick = { onNavigate(R.id.action_homeFragment_to_faqFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_info_24),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.settings),
            onClick = { onNavigate(R.id.action_homeFragment_to_settingsFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_settings_24),
        )
        DrawerMenuItem(
            text = stringResource(id = R.string.about),
            onClick = { onNavigate(R.id.action_homeFragment_to_aboutFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_info_24),
        )
    }
}

@Composable
fun DrawerProfileScreen(
    modifier: Modifier = Modifier,
    username: String,
    onNavigate: (Int, String?, String?) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        DrawerMenuItem(
            text = stringResource(id = R.string.logout),
            onClick = { onLogout() },
            icon = painterResource(id = R.drawable.ic_logout),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.add_account),
            onClick = { },
            icon = painterResource(id = R.drawable.ic_add),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.repositories),
            onClick = { onNavigate(R.id.action_homeFragment_to_profileFragment, username, "2") },
            icon = painterResource(id = R.drawable.baseline_book_24),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.starred),
            onClick = { onNavigate(R.id.action_homeFragment_to_profileFragment, username, "3") },
            icon = painterResource(id = R.drawable.baseline_star_24),
        )
        Divider()
        DrawerMenuItem(
            text = stringResource(id = R.string.pinned),
            onClick = { onNavigate(R.id.action_homeFragment_to_pinnedFragment, null, null) },
            icon = painterResource(id = R.drawable.baseline_bookmark_border_24),
        )
    }
}

@Composable
fun DrawerMenuItem(
    modifier: Modifier = Modifier,
    text: String,
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
            text = text,
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
                text = stringResource(id = R.string.profile),
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
                    .background(JetHubTheme.colors.background.primary),
                closeDrawer = { },
                username = "HasanAnorov",
                onLogout = {},
                onNavigate = { _, _, _ -> }
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
            DrawerBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JetHubTheme.colors.background.plain),
                closeDrawer = { },
                username = "HasanAnorov",
                onLogout = {},
                onNavigate = { _, _, _ -> }
            )
        }
    }
}

