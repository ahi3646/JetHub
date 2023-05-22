package com.hasan.jetfasthub.screens.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.AppScreens
import com.hasan.jetfasthub.screens.main.home.events.received_model.ReceivedEventsItem
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        val username = "HasanAnorov"
        val page = 1
        homeViewModel.getReceivedEvents(token, username)
        homeViewModel.getEvents(token, username)

        return ComposeView(requireContext()).apply {
            setContent {
                val state by homeViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onBottomBarItemSelected = homeViewModel::onBottomBarItemSelected,
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    state: HomeScreenState,
    onBottomBarItemSelected: (AppScreens) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            backgroundColor = Color.White,
            content = {
                TopAppBarContent(scaffoldState, scope)
            },
        )
    }, bottomBar = {
        BottomNav(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            onBottomBarItemSelected = onBottomBarItemSelected,
        )
    }, drawerContent = {
        ModalDrawerSheet {
            Text("Drawer title", modifier = Modifier.padding(16.dp))
            Divider()
            NavigationDrawerItem(label = { Text(text = "Drawer Item") },
                selected = false,
                onClick = { /*TODO*/ })
            // ...other drawer items
        }
    }, content = { contentPadding ->
        when (state.selectedBottomBarItem) {
            AppScreens.Feeds -> FeedsScreen(state.receivedEventsState)
            AppScreens.Issues -> IssuesScreen()
            AppScreens.PullRequests -> PullRequestScreen()
        }
    })
}

@Composable
fun TopAppBarContent(state: ScaffoldState, scope: CoroutineScope) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            scope.launch {
                state.drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
            Log.d("ahi3646", "TopAppBarContent:${state.drawerState.currentValue} ")
        }) {
            Icon(Icons.Rounded.Menu, contentDescription = "Localized description")
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
            Icon(Icons.Outlined.Notifications, contentDescription = "Notification")
        }

        IconButton(onClick = { }) {
            Icon(Icons.Rounded.Search, contentDescription = "Notification")
        }

    }
}

@Composable
fun BottomNav(
    modifier: Modifier,
    onBottomBarItemSelected: (AppScreens) -> Unit,
) {
    Surface(elevation = 16.dp) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomAppBar(containerColor = Color.White) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_github),
                            contentDescription = "Feed Screen"
                        )
                    },
                    label = { Text("Feeds") },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.Feeds) },
                )

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                            contentDescription = "Issues Screen"
                        )
                    },
                    label = { Text("Issues") },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.Issues) },
                )

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                            contentDescription = "PullRequest Screen"
                        )
                    },
                    label = { Text("Pull Requests") },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.PullRequests) },
                )
            }
        }
    }
}

@Composable
fun FeedsScreen(
    receivedEventsState: ReceivedEventsState
) {
    when (receivedEventsState) {

        is ReceivedEventsState.Loading -> {
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

        is ReceivedEventsState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(receivedEventsState.events) { eventItem ->
                        ItemEventCard(eventItem) {
                            Log.d("ahi3646", "FeedsScreen: click ")
                        }
                    }
                }
            }
        }

        is ReceivedEventsState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong - ${receivedEventsState.message}")
            }
        }
    }
}

@Composable
fun ItemEventCard(
    eventItem: ReceivedEventsItem,
    onItemClicked: (eventItem: ReceivedEventsItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked(eventItem) }),
        elevation = 0.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            GlideImage(
                imageModel = { eventItem.actor.avatar_url }, // loading a network image using an URL.
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
                    text = eventItem.actor.login,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.material.MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = eventItem.repo.name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = Color.Black,
                    style = androidx.compose.material.MaterialTheme.typography.caption
                )

            }
        }
    }
}

@Composable
fun IssuesScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Issues Screen")
    }
}

@Composable
fun PullRequestScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "PullScreen Screen")
    }
}