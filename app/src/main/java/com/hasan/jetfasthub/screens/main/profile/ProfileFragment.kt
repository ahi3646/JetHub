package com.hasan.jetfasthub.screens.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.profile.model.event_model.UserEvents
import com.hasan.jetfasthub.screens.main.profile.model.event_model.UserEventsItem
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModel
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModelItem
import com.hasan.jetfasthub.screens.main.profile.model.repo_model.RepositoryModelItem
import com.hasan.jetfasthub.screens.main.profile.model.repo_model.UserRepositoryModel
import com.hasan.jetfasthub.screens.main.search.models.repository_model.Item
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.Constants.chooseFromEvents
import com.hasan.jetfasthub.utility.FileSizeCalculator
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

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
        profileViewModel.getUserEvents(token, username)
        profileViewModel.getUserRepositories(token, username)

        return ComposeView(requireContext()).apply {
            setContent {
                val state by profileViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onNavigate = { dest -> findNavController().navigate(dest) },
                        onListItemClicked = { dest ->
                            Toast.makeText(requireContext(), dest, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    state: ProfileScreenState,
    onNavigate: (Int) -> Unit,
    onListItemClicked: (String) -> Unit
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
        TabScreen(contentPadding, state, onListItemClicked)
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
fun TabScreen(
    contentPaddingValues: PaddingValues,
    state: ProfileScreenState,
    onListItemClicked: (String) -> Unit
) {

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

            1 -> FeedScreen(state.UserEvents, onFeedsItemClicked = onListItemClicked)
            2 -> RepositoriesScreen(
                state.UserRepositories,
                onRepositoryItemClicked = onListItemClicked
            )

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

                if (overviewScreenState.user.blog != null && overviewScreenState.user.blog.toString()
                        .isNotEmpty()
                ) {
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
fun FeedScreen(userEvents: Resource<UserEvents>, onFeedsItemClicked: (String) -> Unit) {
    when (userEvents) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...")
            }
        }

        is Resource.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(userEvents.data!!) { index, UserEventsItem ->
                    FeedsItem(onFeedsItemClicked = onFeedsItemClicked, UserEventsItem)
                    if (index < userEvents.data.lastIndex) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                        )
                    }
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
            }
        }
    }
}

@Composable
fun FeedsItem(
    onFeedsItemClicked: (String) -> Unit,
    userEventsItem: UserEventsItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onFeedsItemClicked(userEventsItem.actor.login)
            })
            .padding(4.dp), elevation = 0.dp, backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                Text(
                    text = buildAnnotatedString {
                        append(userEventsItem.actor.login)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                " " + stringResource(id = chooseFromEvents(userEventsItem.type).action).lowercase(
                                    Locale.getDefault()
                                ) + " "
                            )
                        }
                        append(userEventsItem.repo.name)
                    },
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = Color.Black,
                    style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (userEventsItem.payload.commits != null) {
                    Text(
                        text = userEventsItem.payload.commits.size.toString() + " commits",
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = Color.Black,
                        style = androidx.compose.material.MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = userEventsItem.payload.commits[0].message,
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = Color.Black,
                        style = androidx.compose.material.MaterialTheme.typography.caption,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = chooseFromEvents("eventItem.type").icon),
                        contentDescription = stringResource(
                            id = chooseFromEvents(userEventsItem.type).action
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = ParseDateFormat.getTimeAgo(userEventsItem.created_at).toString(),
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = Color.Black,
                        style = androidx.compose.material.MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun RepositoriesScreen(
    userRepositories: Resource<UserRepositoryModel>,
    onRepositoryItemClicked: (String) -> Unit
) {
    when (userRepositories) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...")
            }
        }

        is Resource.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(userRepositories.data!!) { index, UserEventsItem ->
                    RepositoryItem(
                        UserEventsItem,
                        onRepositoryItemClicked = onRepositoryItemClicked
                    )
                    if (index < userRepositories.data.lastIndex) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                        )
                    }
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
            }
        }
    }
}

@Composable
fun RepositoryItem(
    repository: RepositoryModelItem, onRepositoryItemClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onRepositoryItemClicked(repository.full_name)
            })
            .padding(4.dp), elevation = 0.dp, backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                Text(
                    text = repository.full_name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = Color.Black,
                    style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star_small),
                        contentDescription = "star icon"
                    )

                    Text(
                        text = repository.stargazers_count.toString(),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 2.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_fork_small),
                        contentDescription = "star icon"
                    )

                    Text(
                        text = repository.forks_count.toString(),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 2.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_time_small),
                        contentDescription = "time icon"
                    )

                    Text(
                        text = ParseDateFormat.getTimeAgo(repository.updated_at).toString(),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 2.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_storage_small),
                        contentDescription = "storage icon"
                    )

                    Text(
                        text = FileSizeCalculator.humanReadableByteCountBin(repository.size.toLong()),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 2.dp)
                    )

                    Text(
                        text = repository.language ?: "",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
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