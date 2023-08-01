package com.hasan.jetfasthub.screens.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.TabRow
import androidx.compose.material.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.utility.Constants
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DrawerHeader(user: Resource<GitHubUser>) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)
    ) {

        GlideImage(
            failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
            imageModel = { user.data?.avatar_url },
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            imageOptions = ImageOptions(
                contentDescription = "avatar picture",
                contentScale = ContentScale.Crop,
            )
        )

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 24.dp)
        ) {
            Text(user.data?.name.toString())
            Text(user.data?.login.toString())
        }
    }

}

@Composable
fun DrawerBody(
    closeDrawer: () -> Unit,
    username: String,
    onLogout: () -> Unit,
    onNavigate: (Int, String?, String?) -> Unit
){

    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("MENU", "PROFILE")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = Color.Transparent,
            contentColor = Color.Blue
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        if (tabIndex == index) {
                            Text(title, color = Color.Blue)
                        } else {
                            Text(title, color = Color.Black)
                        }
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }
        when (tabIndex) {
            0 -> DrawerMenuScreen(closeDrawer, username, onNavigate)
            1 -> DrawerProfileScreen(username, onNavigate, onLogout)
        }
    }

}


@Composable
fun DrawerMenuScreen(
    closeDrawer: () -> Unit,
    username: String,
    onNavigate: (Int, String?, String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 2.dp, bottom = 2.dp)
                .clickable {
                    closeDrawer()
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_home_24),
                contentDescription = "home icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Home",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 2.dp, bottom = 2.dp)
                .clickable {
                    closeDrawer()
                    onNavigate(R.id.action_homeFragment_to_profileFragment, username, null)
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Profile icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Profile",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable { }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_people_alt_24),
                contentDescription = "Organizations icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Organizations",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable {
                    onNavigate(R.id.action_homeFragment_to_notificationsFragment, null, null)
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_notifications_24),
                contentDescription = "Notifications icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Notifications",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 2.dp, bottom = 2.dp)
                .clickable { onNavigate(R.id.action_homeFragment_to_pinnedFragment, null, null) }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_bookmark_24),
                contentDescription = "Pinned icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Pinned",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable { }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_trending_up_24),
                contentDescription = "Trending icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Trending",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 4.dp)
                .clickable {
                    onNavigate(R.id.action_homeFragment_to_gistsFragment, username, null)
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_code_24),
                contentDescription = "Gists icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Gists",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Divider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 2.dp, bottom = 2.dp)
                .clickable {
                    onNavigate(
                        R.id.action_homeFragment_to_repositoryFragment,
                        Constants.JetHubOwner,
                        Constants.JetHubRepoName
                    )
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_fasthub_mascot),
                contentDescription = "JetHub icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "JetHub",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable { onNavigate(R.id.action_homeFragment_to_faqFragment, null, null) }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_info_24),
                contentDescription = "FAQ icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "FAQ",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable {
                    onNavigate(R.id.action_homeFragment_to_settingsFragment, null, null)
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = "Setting icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Setting",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable { }) {
            Image(
                painter = painterResource(id = R.drawable.ic_money),
                contentDescription = "Restore Purchases icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Restore Purchases",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(bottom = 2.dp)
                .clickable {
                    onNavigate(R.id.action_homeFragment_to_aboutFragment, null, null)
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_info_24),
                contentDescription = "About icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "About",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
            )
        }
    }
}

@Composable
fun DrawerProfileScreen(
    username: String,
    onNavigate: (Int, String?, String?) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 4.dp, bottom = 2.dp)
                .clickable { onLogout() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = "Logout icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Logout",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 4.dp, bottom = 2.dp)
                .clickable {
                    //implement action
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add Account icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Add Account",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 4.dp, bottom = 2.dp)
                .clickable {
                    onNavigate(R.id.action_homeFragment_to_profileFragment, username, "2")
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_book_24),
                contentDescription = "Repositories icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Repositories",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 4.dp, bottom = 2.dp)
                .clickable {
                    onNavigate(
                        R.id.action_homeFragment_to_profileFragment,
                        username,
                        "3"
                    )
                }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "Starred icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Starred",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(top = 4.dp, bottom = 2.dp)
                .clickable { onNavigate(R.id.action_homeFragment_to_pinnedFragment, null, null) }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                contentDescription = "Pinned icon",
                modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 12.dp),
            )
            Text(
                text = "Pinned",
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 16.sp,
            )
        }
    }
}
