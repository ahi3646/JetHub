package com.hasan.jetfasthub.screens.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModel
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModelItem
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val username = arguments?.getString("username") ?: ""
        Log.d("ahi3646", "onCreateView: $username ")
        val token = PreferenceHelper.getToken(requireContext())
        Log.d("ahi3646", "onCreateView: token - $token")

        profileViewModel.getUser(token, username)
        profileViewModel.getUserOrganisations(token, username)

        return ComposeView(requireContext()).apply {
            setContent {
                val state by profileViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(state = state,
                        onNavigate = { dest -> findNavController().navigate(dest) })
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    state: ProfileScreenState,
    onNavigate: (Int) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 0.dp,
                content = {
                    TopAppBarContent(onNavigate)
                },
            )
        },
    ) { contentPadding ->
        TabScreen(contentPadding, state)
    }
}

@Composable
private fun TopAppBarContent(
    onBackPressed: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            onBackPressed(R.id.action_profileFragment_to_homeFragment)
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
        }

        Text(
            color = Color.Black,
            modifier = Modifier
                .weight(1F)
                .padding(start = 10.dp, end = 10.dp),
            text = "JetHub",
            style = MaterialTheme.typography.titleLarge,
        )

        IconButton(onClick = { }) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }

        IconButton(onClick = { }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "more option")
        }

    }
}


@Composable
fun TabScreen(contentPaddingValues: PaddingValues, state: ProfileScreenState) {

    var tabIndex by remember { mutableStateOf(0) }
    val tabs =
        listOf("OVERVIEW", "FEED", "REPOSITORIES", "STARRED", "GISTS", "FOLLOWERS", "FOLLOWING")

    Column(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxWidth()
    ) {
        ScrollableTabRow(selectedTabIndex = tabIndex, containerColor = Color.White) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }
        when (tabIndex) {
            0 -> OverviewScreen(
                state.OverviewScreenState,
                state.Organisations
            )

            1 -> FeedScreen()
            2 -> RepositoriesScreen()
            3 -> StarredScreen()
            4 -> GistsScreen()
            5 -> FollowersScreen()
            6 -> FollowingScreen()
        }
    }

}

@Composable
fun OverviewScreen(
    overviewScreenState: UserOverviewScreen,
    organisation: Resource<OrgModel>
) {

    when (overviewScreenState) {
        is UserOverviewScreen.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...")
            }
        }

        is UserOverviewScreen.Content -> {
            /**
            Column(
            modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
            ) {

            Row(
            modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            ) {

            GlideImage(
            imageModel = { userScreenState.user.avatar_url }, // loading a network image using an URL.
            modifier = Modifier
            .size(80.dp, 80.dp)
            .clip(RoundedCornerShape(16.dp)),
            imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.CenterStart,
            contentDescription = "Actor Avatar"
            )
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
            text = userScreenState.user.name,
            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style = androidx.compose.material.MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
            text = userScreenState.user.login,
            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
            color = Color.Black,
            style = androidx.compose.material.MaterialTheme.typography.caption
            )

            }
            }

            Text(
            text = userScreenState.user.bio,
            modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
            textAlign = TextAlign.Start
            )

            Row(
            modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
            ) {
            Text(
            text = "Following - ${userScreenState.user.following}",
            modifier = Modifier.padding(8.dp)
            )

            Divider(
            color = Color.Black,
            modifier = Modifier
            .height(16.dp)
            .width(2.dp),
            )

            Text(
            text = "Followers - ${userScreenState.user.followers}",
            modifier = Modifier.padding(8.dp)
            )
            }

            Button(onClick = {

            }, modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {
            Text(text = "Unfollow")
            }

            Row(
            modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
            ) {
            Icon(imageVector = Icons.Filled.Person, contentDescription = "Corporation")
            Text(
            text = userScreenState.user.company,
            modifier = Modifier.padding(start = 16.dp)
            )
            }

            Divider(
            color = Color.Black,
            modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
            )

            Row(
            modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
            ) {
            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Corporation")
            Text(
            text = userScreenState.user.location,
            modifier = Modifier.padding(start = 16.dp)
            )
            }

            Divider(
            color = Color.Black,
            modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
            )

            Row(
            modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
            ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Corporation")
            Text(
            text = userScreenState.user.updated_at,
            modifier = Modifier.padding(start = 16.dp)
            )
            }

            Divider(
            color = Color.Black,
            modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
            )


            }
             */

            var offset by remember { mutableStateOf(0f) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .scrollable(orientation = Orientation.Vertical,
                        // Scrollable state: describes how to consume
                        // scrolling delta and update offset
                        state = rememberScrollableState { delta ->
                            offset += delta
                            delta
                        })
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    GlideImage(
                        imageModel = {
                            overviewScreenState.user.avatar_url
                        }, // loading a network image using an URL.
                        modifier = Modifier
                            .size(80.dp, 80.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.CenterStart,
                            contentDescription = "Actor Avatar"
                        )
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = overviewScreenState.user.name,
                            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = androidx.compose.material.MaterialTheme.typography.subtitle1
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = overviewScreenState.user.login,
                            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                            color = Color.Black,
                            style = androidx.compose.material.MaterialTheme.typography.caption
                        )

                    }
                }

                if (overviewScreenState.user.bio != null) {
                    Text(
                        text = overviewScreenState.user.bio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Start
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Following - ${overviewScreenState.user.following}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Log.d(
                        "ahi3646",
                        "OverviewScreen: following - ${overviewScreenState.user.following} "
                    )

                    Divider(
                        color = Color.Black,
                        modifier = Modifier
                            .height(16.dp)
                            .width(2.dp),
                    )

                    Text(
                        text = "Followers - ${overviewScreenState.user.followers}",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Button(
                    onClick = {}, modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_follow),
                        contentDescription = "follow button"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Unfollow")
                }

                if (overviewScreenState.user.company != null) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_group),
                            contentDescription = "Corporation"
                        )
                        Text(
                            text = overviewScreenState.user.company,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Divider(
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 2.dp, bottom = 2.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Corporation")
                    Text(
                        text = overviewScreenState.user.location,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 2.dp, bottom = 2.dp)
                )

                if (overviewScreenState.user.email != null) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email"
                        )
                        Text(
                            text = overviewScreenState.user.email.toString(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    Divider(
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 2.dp, bottom = 2.dp)
                    )
                }

                if (overviewScreenState.user.blog != null && overviewScreenState.user.blog.toString().isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_insert_link),
                            contentDescription = "Corporation"
                        )
                        Text(
                            text = overviewScreenState.user.blog.toString(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Divider(
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 2.dp, bottom = 2.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = "Corporation"
                    )
                    Text(
                        text = ParseDateFormat.getTimeAgo(overviewScreenState.user.created_at)
                            .toString(), modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Text(
                    text = "Organisations",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start),
                    fontSize = 18.sp
                )

                when (organisation) {
                    is Resource.Success -> {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(organisation.data!!) { organization ->
                                OrganisationItem(organization)
                            }
                        }
                    }

                    else -> {
                        Text(text = "Cannot load organisations!")
                    }
                }

                /**
                Text(
                text = "Pinned",
                modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start),
                fontSize = 18.sp
                )

                LazyColumn(
                modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
                ) {
                itemsIndexed(arrayOf("a", "b")) { index, item ->

                }
                }

                Text(
                text = "Contributions",
                modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start),
                fontSize = 18.sp
                )
                 */

            }

        }

        is UserOverviewScreen.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong - ${overviewScreenState.message}")
            }
        }
    }
}

@Composable
fun OrganisationItem(organisation: OrgModelItem) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(8.dp)
    ) {

        GlideImage(
            imageModel = { organisation.avatar_url }, // loading a network image using an URL.
            modifier = Modifier
                .size(48.dp, 48.dp)
                .clip(RoundedCornerShape(16.dp)),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.CenterStart,
                contentDescription = "Actor Avatar"
            )
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(text = organisation.login, textAlign = TextAlign.Center)
    }
}

@Composable
fun FeedScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Feed")
    }
}

@Composable
fun RepositoriesScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "RepositoriesScreen")
    }
}

@Composable
fun StarredScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Starred")
    }
}

@Composable
fun GistsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Gists")
    }
}

@Composable
fun FollowersScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Followers")
    }
}

@Composable
fun FollowingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Following")
    }
}




