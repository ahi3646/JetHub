package com.hasan.jetfasthub.screens.main.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.home.received_events_model.ReceivedEventsModelItem
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesItem
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.ui.theme.RippleCustomTheme
import com.hasan.jetfasthub.utility.Constants.chooseFromEvents
import com.hasan.jetfasthub.utility.IssueState
import com.hasan.jetfasthub.utility.MyIssuesType
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.RepoQueryProvider
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var token: String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        token = PreferenceHelper.getToken(requireContext())

        homeViewModel.getAuthenticatedUser(token)
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { authenticatedUser ->
                PreferenceHelper.saveAuthenticatedUser(requireContext(), authenticatedUser.login)

                homeViewModel.getUser(token, authenticatedUser.login)
                homeViewModel.getReceivedEvents(token, authenticatedUser.login)


                homeViewModel.getIssuesWithCount(
                    token = token,
                    query = getUrlForIssues(
                        MyIssuesType.CREATED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.CREATED
                )

                homeViewModel.getIssuesWithCount(
                    token = token,
                    query = getUrlForIssues(
                        MyIssuesType.ASSIGNED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.ASSIGNED
                )

                homeViewModel.getIssuesWithCount(
                    token = token,
                    query = getUrlForIssues(
                        MyIssuesType.MENTIONED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.MENTIONED
                )

                homeViewModel.getIssuesWithCount(
                    token = token,
                    query = getUrlForIssues(
                        MyIssuesType.PARTICIPATED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.PARTICIPATED
                )


                homeViewModel.getPullsWithCount(
                    token = token,
                    query = getUrlForPulls(
                        MyIssuesType.CREATED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.CREATED
                )

                homeViewModel.getPullsWithCount(
                    token = token,
                    query = getUrlForPulls(
                        MyIssuesType.ASSIGNED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.ASSIGNED
                )

                homeViewModel.getPullsWithCount(
                    token = token,
                    query = getUrlForPulls(
                        MyIssuesType.MENTIONED,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.MENTIONED
                )

                homeViewModel.getPullsWithCount(
                    token = token,
                    query = getUrlForPulls(
                        MyIssuesType.REVIEW,
                        IssueState.Open,
                        authenticatedUser.login
                    ),
                    page = 1,
                    issuesType = MyIssuesType.REVIEW
                )

            }
            .launchIn(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val state by homeViewModel.state.collectAsState()

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scaffoldState = rememberScaffoldState(drawerState = drawerState)
                val drawerScope = rememberCoroutineScope()

                activity?.onBackPressedDispatcher?.addCallback(
                    viewLifecycleOwner,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (scaffoldState.drawerState.isOpen) {
                                drawerScope.launch {
                                    drawerState.close()
                                }
                            } else {
                                isEnabled = false
                                activity?.onBackPressedDispatcher!!.onBackPressed()
                            }
                        }
                    }
                )

                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onBottomBarItemSelected = homeViewModel::onBottomBarItemSelected,
                        onNavigate = { dest, data, extra ->
                            when (dest) {

                                -1 -> {
                                    findNavController().popBackStack()
                                }

                                R.id.action_homeFragment_to_notificationsFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_searchFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_repositoryFragment -> {
                                    val bundle = Bundle()
                                    bundle.putString("repository_owner", data)
                                    bundle.putString("repository_name", extra)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_homeFragment_to_profileFragment -> {
                                    Log.d("ahi3646", "onCreateView: $data -- $extra ")
                                    val bundle = Bundle()
                                    bundle.putString("username", data)
                                    bundle.putString("start_index", extra)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_homeFragment_to_pinnedFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_gistsFragment -> {
                                    val bundle = bundleOf("gist_data" to data)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_homeFragment_to_faqFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_settingsFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_aboutFragment -> {
                                    findNavController().navigate(dest)
                                }

                            }
                        },
                        onIssueItemClicked = {dest, owner, repo, issueNumber ->
                            Log.d("ahi3646", "onCreateView: $owner  $repo  $issueNumber ")
                            val bundle = Bundle()
                            bundle.putString("issue_owner", owner)
                            bundle.putString("issue_repo", repo)
                            bundle.putString("issue_number", issueNumber)
                            findNavController().navigate(dest, bundle)
                        },
                        onIssuesStateChanged = { issuesType, issueState ->
                            homeViewModel.getIssuesWithCount(
                                token = token,
                                query = getUrlForIssues(
                                    issuesType,
                                    issueState,
                                    state.user.data!!.login
                                ),
                                page = 1,
                                issuesType = issuesType
                            )
                        },
                        onPullsStateChanged = { myIssuesType, issueState ->
                            homeViewModel.getPullsWithCount(
                                token = token,
                                query = getUrlForPulls(
                                    myIssuesType,
                                    issueState,
                                    state.user.data!!.login
                                ),
                                page = 1,
                                issuesType = myIssuesType
                            )
                        },
                        scaffoldState = scaffoldState,
                        onNavigationClick = {
                            drawerScope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun getUrlForPulls(
        issueType: MyIssuesType,
        issueState: IssueState,
        login: String
    ): String {
        when (issueType) {
            MyIssuesType.CREATED -> return RepoQueryProvider.getMyIssuesPullRequestQuery(
                login,
                issueState,
                true
            )

            MyIssuesType.ASSIGNED -> return RepoQueryProvider.getAssigned(login, issueState, true)
            MyIssuesType.MENTIONED -> return RepoQueryProvider.getMentioned(login, issueState, true)
            MyIssuesType.REVIEW -> return RepoQueryProvider.getReviewRequests(login, issueState)
            else -> {}
        }
        return RepoQueryProvider.getMyIssuesPullRequestQuery(login, issueState, false)
    }

    private fun getUrlForIssues(
        issueType: MyIssuesType,
        issueState: IssueState,
        login: String
    ): String {
        when (issueType) {
            MyIssuesType.CREATED -> return RepoQueryProvider.getMyIssuesPullRequestQuery(
                login,
                issueState,
                false
            )

            MyIssuesType.ASSIGNED -> return RepoQueryProvider.getAssigned(login, issueState, false)
            MyIssuesType.MENTIONED -> return RepoQueryProvider.getMentioned(
                login,
                issueState,
                false
            )

            MyIssuesType.PARTICIPATED -> return RepoQueryProvider.getParticipated(
                login,
                issueState,
                false
            )

            else -> {}
        }
        return RepoQueryProvider.getMyIssuesPullRequestQuery(login, issueState, false)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContent(
    state: HomeScreenState,
    onBottomBarItemSelected: (AppScreens) -> Unit,
    onNavigate: (Int, String?, String?) -> Unit,
    onIssueItemClicked: (Int, String, String, String) -> Unit,
    onIssuesStateChanged: (MyIssuesType, IssueState) -> Unit,
    onPullsStateChanged: (MyIssuesType, IssueState) -> Unit,
    scaffoldState: ScaffoldState,
    onNavigationClick: () -> Unit
) {
    val sheetScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetContent = {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Are you sure ?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { sheetScope.launch { sheetState.collapse() } }) {
                        Text(text = "Ok")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = { sheetScope.launch { sheetState.collapse() } }) {
                        Text(text = "No")
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    sheetScope.launch {
                        if (sheetState.isExpanded) {
                            sheetState.collapse()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Scaffold(
            modifier = Modifier
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (sheetState.isExpanded) {
                                sheetScope.launch {
                                    sheetState.collapse()
                                }
                            }
                        }
                    )
                },
            scaffoldState = scaffoldState,
            topBar = { AppBar(onNavigationClick, onNavigate) },
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            drawerContent = {
                DrawerHeader(user = state.user)
                DrawerBody(
                    closeDrawer = onNavigationClick,
                    username = state.user.data?.login ?: "",
                    onLogout = {
                        onNavigationClick()
                        sheetScope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    },
                    onNavigate = onNavigate
                )
            },
            bottomBar = {
                BottomNav(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(58.dp),
                    onBottomBarItemSelected = onBottomBarItemSelected,
                )
            },
        ) {
            when (state.selectedBottomBarItem) {
                AppScreens.Feeds -> FeedsScreen(
                    contentPaddingValues = it,
                    receivedEventsState = state.receivedEventsState,
                    onNavigate = onNavigate
                )

                AppScreens.Issues -> IssuesScreen(
                    contentPaddingValues = it,
                    issuesCreated = state.IssuesCreated,
                    issuesAssigned = state.IssuesAssigned,
                    issuesMentioned = state.IssuesMentioned,
                    issuesParticipated = state.IssuesParticipated,
                    onIssueItemClicked = onIssueItemClicked,
                    onIssuesStateChanged = onIssuesStateChanged
                )

                AppScreens.PullRequests -> PullRequestScreen(
                    contentPaddingValues = it,
                    PullsCreated = state.PullsCreated,
                    PullsAssigned = state.PullsAssigned,
                    PullsMentioned = state.PullsMentioned,
                    PullsReview = state.PullsReview,
                    onNavigate = onNavigate,
                    onPullsStateChanges = onPullsStateChanged
                )
            }
        }
    }
}

@Composable
fun BottomNav(
    modifier: Modifier,
    onBottomBarItemSelected: (AppScreens) -> Unit,
) {
    Surface(
        elevation = 16.dp,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomAppBar(contentColor = MaterialTheme.colorScheme.surface) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_github),
                            contentDescription = "Feed Screen",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = { Text("Feeds", color = MaterialTheme.colorScheme.onSurface) },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.Feeds) },
                )

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                            contentDescription = "Issues Screen",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = { Text("Issues", color = MaterialTheme.colorScheme.onSurface) },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.Issues) },
                )

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                            contentDescription = "PullRequest Screen",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = { Text("Pull Requests", color = MaterialTheme.colorScheme.onSurface) },
                    selected = false,
                    onClick = { onBottomBarItemSelected(AppScreens.PullRequests) },
                )
            }
        }
    }
}

@Composable
fun FeedsScreen(
    contentPaddingValues: PaddingValues,
    receivedEventsState: ReceivedEventsState,
    onNavigate: (Int, String?, String?) -> Unit
) {
    when (receivedEventsState) {

        is ReceivedEventsState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is ReceivedEventsState.Success -> {
            val events = receivedEventsState.events
            if (events.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(contentPaddingValues)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(events) { eventItem ->
                        ItemEventCard(eventItem, onNavigate)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No feeds")
                }
            }
        }

        is ReceivedEventsState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
            }
        }

    }
}

@Composable
private fun ItemEventCard(
    eventItem: ReceivedEventsModelItem,
    onNavigate: (Int, String?, String?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                val uri = Uri.parse(eventItem.repo.url).lastPathSegment
                val parentUsername = Uri.parse(eventItem.repo.url).pathSegments[1]

                when (eventItem.type) {
                    "ForkEvent" -> {
                        onNavigate(
                            R.id.action_homeFragment_to_repositoryFragment,
                            eventItem.actor.login,
                            eventItem.payload.forkee.name,
                        )
                    }

                    "ReleaseEvent" -> {
                        onNavigate(
                            R.id.action_homeFragment_to_repositoryFragment,
                            parentUsername,
                            uri,
                        )
                    }

                    else -> {
                        onNavigate(
                            R.id.action_homeFragment_to_repositoryFragment,
                            parentUsername,
                            uri,
                        )
                    }
                }
            },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {

            GlideImage(
                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                imageModel = {
                    eventItem.actor.avatar_url
                }, // loading a network image using an URL.
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .size(48.dp, 48.dp)
                    .clip(CircleShape)
                    .clickable {
                        Log.d("ahi3646", "ItemEventCard: user login - ${eventItem.actor.login} ")
                        onNavigate(
                            R.id.action_homeFragment_to_profileFragment,
                            eventItem.actor.login,
                            null
                        )
                    },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterStart,
                    contentDescription = "Actor Avatar"
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(eventItem.actor.login)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                " " + stringResource(id = chooseFromEvents(eventItem.type).action).lowercase(
                                    Locale.getDefault()
                                ) + " "
                            )
                        }
                        append(eventItem.repo.name)
                    },
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                        painter = painterResource(id = chooseFromEvents(eventItem.type).icon),
                        contentDescription = stringResource(
                            id = chooseFromEvents(eventItem.type).action
                        ),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = ParseDateFormat.getTimeAgo(eventItem.created_at).toString(),
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
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
fun IssuesScreen(
    contentPaddingValues: PaddingValues,
    issuesCreated: Resource<IssuesModel>,
    issuesAssigned: Resource<IssuesModel>,
    issuesMentioned: Resource<IssuesModel>,
    issuesParticipated: Resource<IssuesModel>,
    onIssueItemClicked: (Int, String, String, String) -> Unit,
    onIssuesStateChanged: (MyIssuesType, IssueState) -> Unit
) {

    when (issuesCreated) {

        is Resource.Loading -> {
            var tabIndex by remember { mutableIntStateOf(0) }
            val tabs = listOf("CREATED", "ASSIGNED", "MENTIONED", "PARTICIPATED")

            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = tabIndex
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = {
                                if (tabIndex == index) {
                                    Text(
                                        title,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    Text(title, color = MaterialTheme.colorScheme.outline)
                                }
                            },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Success -> {
            var tabIndex by remember { mutableIntStateOf(0) }
            val issuesCreatedData = issuesCreated.data!!
            val issuesAssignedData = issuesAssigned.data!!
            val issuesMentionedData = issuesMentioned.data!!
            val issuesParticipatedData = issuesParticipated.data!!

            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                    ScrollableTabRow(
                        selectedTabIndex = tabIndex
                    ) {
                        Tab(
                            selected = tabIndex == 0,
                            onClick = { tabIndex = 0 }
                        ) {
                            TabItem(
                                issuesCount = issuesCreatedData.total_count ?: 0,
                                tabIndex = tabIndex,
                                index = 0,
                                tabName = "CREATED",
                                onItemClick = {
                                    tabIndex = 0
                                },
                                onStateChanged = { state ->
                                    onIssuesStateChanged(MyIssuesType.CREATED, state)
                                    Log.d("ahi3646", "IssuesScreen: $state ")
                                },
                            )
                        }

                        Tab(
                            selected = tabIndex == 0,
                            onClick = { tabIndex = 0 }
                        ) {
                            TabItem(
                                issuesCount = issuesAssignedData.total_count ?: 0,
                                tabIndex = tabIndex,
                                index = 1,
                                tabName = "ASSIGNED",
                                onItemClick = {
                                    tabIndex = 1
                                },
                                onStateChanged = { state ->
                                    onIssuesStateChanged(MyIssuesType.ASSIGNED, state)
                                    Log.d("ahi3646", "IssuesScreen: $state ")
                                },
                            )
                        }

                        Tab(
                            selected = tabIndex == 0,
                            onClick = { tabIndex = 0 }
                        ) {
                            TabItem(
                                issuesCount = issuesMentionedData.total_count ?: 0,
                                tabIndex = tabIndex,
                                index = 2,
                                tabName = "MENTIONED",
                                onItemClick = {
                                    tabIndex = 2
                                },
                                onStateChanged = { state ->
                                    onIssuesStateChanged(MyIssuesType.MENTIONED, state)
                                    Log.d("ahi3646", "IssuesScreen: $state ")
                                },
                            )
                        }

                        Tab(
                            selected = tabIndex == 0,
                            onClick = { tabIndex = 0 }
                        ) {
                            TabItem(
                                issuesCount = issuesParticipatedData.total_count ?: 0,
                                tabIndex = tabIndex,
                                index = 3,
                                tabName = "PARTICIPATED",
                                onItemClick = {
                                    tabIndex = 3
                                },
                                onStateChanged = { state ->
                                    onIssuesStateChanged(MyIssuesType.PARTICIPATED, state)
                                    Log.d("ahi3646", "IssuesScreen: $state ")
                                },
                            )
                        }

                    }
                }

                when (tabIndex) {
                    0 -> IssuesCreated(issuesCreatedData, onIssueItemClicked)
                    1 -> IssuesAssigned(issuesAssignedData, onIssueItemClicked)
                    2 -> IssuesMentioned(issuesMentionedData, onIssueItemClicked)
                    3 -> IssuesParticipated(issuesParticipatedData, onIssueItemClicked)
                }
            }
        }

        is Resource.Failure -> {
            var tabIndex by remember { mutableIntStateOf(0) }
            val tabs = listOf("CREATED", "ASSIGNED", "MENTIONED", "PARTICIPATED")

            Column(
                modifier = Modifier
                    .padding(contentPaddingValues)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {

                ScrollableTabRow(
                    selectedTabIndex = tabIndex
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = {
                                if (tabIndex == index) {
                                    Text(
                                        title,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    Text(title, color = MaterialTheme.colorScheme.outline)
                                }
                            },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Can't load data!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

    }
}

@Composable
private fun IssuesItem(
    issue: IssuesItem,
    onIssueItemClicked: (Int, String, String, String) -> Unit
) {
    val repoUrl = Uri.parse(issue.repository_url).pathSegments
    val repoName = repoUrl[repoUrl.lastIndex - 1] + "/" + repoUrl[repoUrl.lastIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onIssueItemClicked(
                        R.id.action_homeFragment_to_issueFragment,
                        repoUrl[repoUrl.lastIndex - 1],
                        repoUrl[repoUrl.lastIndex],
                        issue.number.toString()
                    )
                }
            )
            .padding(8.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = issue.title,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = buildAnnotatedString {
                            append(repoName)
                            append("#${issue.number}")
                            append(" ")
                            append(issue.state)
                            append(" ")
                            if (issue.closed_at != null) {
                                append(
                                    ParseDateFormat.getTimeAgo(issue.closed_at.toString())
                                        .toString()
                                )
                            }
                        },
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.weight(1F),
                        fontSize = 12.sp
                    )

                    if (issue.comments != 0) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_comment_small),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = issue.comments.toString(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun TabItem(
    issuesCount: Int,
    tabIndex: Int,
    index: Int,
    tabName: String,
    onItemClick: () -> Unit,
    onStateChanged: (IssueState) -> Unit,
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isOpened by rememberSaveable {
        mutableStateOf(true)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Column(
        modifier = Modifier.onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick()
                    isContextMenuVisible = !isContextMenuVisible
                }
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
//                use this to handle long click, swipe, double tap and many other
//                .pointerInput( -- your state -- ){}
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_issue_opened_small),
                    contentDescription = null,
                    tint = if (isOpened) colorResource(id = R.color.material_green_700) else colorResource(
                        id = R.color.material_red_700
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "$tabName ($issuesCount)",
                    color = if (tabIndex == index) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_dropdown_icon),
                    contentDescription = null,
                    tint = if (tabIndex == index) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.outline
                )

            }

            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        onStateChanged(IssueState.Open)
                        isOpened = true
                        isContextMenuVisible = false
                    }
                ) {
                    Text(text = "Opened", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                DropdownMenuItem(
                    onClick = {
                        onStateChanged(IssueState.Closed)
                        isOpened = false
                        isContextMenuVisible = false
                    }
                ) {
                    Text(text = "Closed", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

        }
    }

}

@Composable
private fun IssuesCreated(
    issuesModel: IssuesModel,
    onIssueItemClicked: (Int, String, String, String) -> Unit
) {
    val issues = issuesModel.items
    if (issues.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(issues) { issue ->
                IssuesItem(issue, onIssueItemClicked)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No issues", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun IssuesAssigned(
    issuesModel: IssuesModel,
    onIssueItemClicked: (Int, String, String, String) -> Unit
) {
    val issues = issuesModel.items
    if (issues.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(issues) { issue ->
                IssuesItem(issue, onIssueItemClicked)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No issues", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun IssuesMentioned(
    issuesModel: IssuesModel,
    onIssueItemClicked: (Int, String, String, String) -> Unit
) {
    val issues = issuesModel.items
    if (issues.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(issues) { issue ->
                IssuesItem(issue, onIssueItemClicked)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No issues", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun IssuesParticipated(
    issuesModel: IssuesModel,
    onIssueItemClicked: (Int, String, String, String) -> Unit
) {
    val issues = issuesModel.items
    if (issues.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(issues) { issue ->
                IssuesItem(issue, onIssueItemClicked)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No issues, color = MaterialTheme.colorScheme.onSurfaceVariant")
        }
    }
}


@Composable
fun PullRequestScreen(
    contentPaddingValues: PaddingValues,
    PullsCreated: Resource<IssuesModel>,
    PullsAssigned: Resource<IssuesModel>,
    PullsMentioned: Resource<IssuesModel>,
    PullsReview: Resource<IssuesModel>,
    onNavigate: (Int, String?, String?) -> Unit,
    onPullsStateChanges: (MyIssuesType, IssueState) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        ScrollableTabRow(
            selectedTabIndex = tabIndex
        ) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = 0,
                    tabIndex = tabIndex,
                    index = 0,
                    tabName = "CREATED",
                    onItemClick = {
                        tabIndex = 0
                    },
                    onStateChanged = { state ->
                        onPullsStateChanges(MyIssuesType.CREATED, state)
                        Log.d("ahi3646", "IssuesScreen: $state ")
                    },
                )
            }

            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = 0,
                    tabIndex = tabIndex,
                    index = 1,
                    tabName = "ASSIGNED",
                    onItemClick = {
                        tabIndex = 1
                    },
                    onStateChanged = { state ->
                        onPullsStateChanges(MyIssuesType.ASSIGNED, state)
                        Log.d("ahi3646", "IssuesScreen: $state ")
                    },
                )
            }

            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = 0,
                    tabIndex = tabIndex,
                    index = 2,
                    tabName = "MENTIONED",
                    onItemClick = {
                        tabIndex = 2
                    },
                    onStateChanged = { state ->
                        onPullsStateChanges(MyIssuesType.MENTIONED, state)
                        Log.d("ahi3646", "IssuesScreen: $state ")
                    },
                )
            }

            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            ) {
                TabItem(
                    issuesCount = 0,
                    tabIndex = tabIndex,
                    index = 3,
                    tabName = "REVIEW REQUESTS",
                    onItemClick = {
                        tabIndex = 3
                    },
                    onStateChanged = { state ->
                        onPullsStateChanges(MyIssuesType.REVIEW, state)
                        Log.d("ahi3646", "IssuesScreen: $state ")
                    },
                )
            }
        }

        when (tabIndex) {
            0 -> PullRequestsCreated(PullsCreated, onNavigate)
            1 -> PullRequestsAssigned(PullsAssigned, onNavigate)
            2 -> PullRequestsMentioned(PullsMentioned, onNavigate)
            3 -> PullRequestsReviewedRequests(PullsReview, onNavigate)
        }
    }
}

@Composable
private fun PullRequestsCreated(
    issuesModel: Resource<IssuesModel>,
    onNavigate: (Int, String?, String?) -> Unit
) {
    when (issuesModel) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val pulls = issuesModel.data!!.items
            if (pulls.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(pulls) { pull ->
                        PullsItem(pull, onNavigate)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No pulls", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PullRequestsAssigned(
    issuesModel: Resource<IssuesModel>,
    onNavigate: (Int, String?, String?) -> Unit
) {
    when (issuesModel) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val pulls = issuesModel.data!!.items
            if (pulls.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(pulls) { pull ->
                        PullsItem(pull, onNavigate)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No pulls", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PullRequestsMentioned(
    issuesModel: Resource<IssuesModel>,
    onNavigate: (Int, String?, String?) -> Unit
) {
    when (issuesModel) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val pulls = issuesModel.data!!.items
            if (pulls.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(pulls) { pull ->
                        PullsItem(pull, onNavigate)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No pulls", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PullRequestsReviewedRequests(
    issuesModel: Resource<IssuesModel>,
    onNavigate: (Int, String?, String?) -> Unit
) {
    when (issuesModel) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val pulls = issuesModel.data!!.items
            if (pulls.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(pulls) { pull ->
                        PullsItem(pull, onNavigate)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No pulls", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PullsItem(
    pull: IssuesItem,
    onNavigate: (Int, String?, String?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onNavigate(R.id.action_homeFragment_to_issueFragment, null, null)
                }
            )
            .padding(8.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Text(
                text = pull.title,
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            val repoUrl = Uri.parse(pull.repository_url).pathSegments
            val repoName = repoUrl[repoUrl.lastIndex - 1] + "/" + repoUrl[repoUrl.lastIndex]

            Row {
                Text(
                    text = buildAnnotatedString {
                        append(repoName)
                        append("#${pull.number}")
                        append(" ")
                        if (pull.state == "closed") {
                            append(pull.state)
                            append(ParseDateFormat.getTimeAgo(pull.closed_at.toString()).toString())
                        } else {
                            append("${pull.state}ed")
                            append(ParseDateFormat.getTimeAgo(pull.created_at).toString())
                        }
                    },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1F),
                    fontSize = 12.sp
                )
            }
        }
    }
}

