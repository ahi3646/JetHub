package com.hasan.jetfasthub.screens.main.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFragment : Fragment() {

    private val repositoryViewModel: RepositoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(requireContext()).apply {
            setContent {
                val state by repositoryViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state,
                        onBottomBarClicked = { repositoryScreen ->
                            repositoryViewModel.onBottomBarItemClicked(repositoryScreen)
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun MainContent(
    state: RepositoryScreenState, onBottomBarClicked: (RepositoryScreens) -> Unit
) {

    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                TitleHeader()
                Toolbar()
            }
        },
        bottomBar = {
            BottomNav(onBottomBarClicked)
        }
    ) { paddingValues ->

        when (state.selectedBottomBarItem) {
            RepositoryScreens.Code -> CodeScreen(paddingValues = paddingValues)
            RepositoryScreens.Issues -> IssuesScreen(paddingValues = paddingValues)
            RepositoryScreens.PullRequest -> PullRequestsScreen(paddingValues = paddingValues)
            RepositoryScreens.Projects -> ProjectsScreen(paddingValues = paddingValues)
        }
    }
}

@Composable
private fun CodeScreen(paddingValues: PaddingValues) {
    val tabs = listOf("README", "FILES", "COMMITS", "RELEASE", "CONTRIBUTORS")
    var tabIndex by remember { mutableStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index, onClick = { tabIndex = index },
                text = {
                    if (tabIndex == index) {
                        androidx.compose.material3.Text(title, color = Color.Blue)
                    } else {
                        androidx.compose.material3.Text(title, color = Color.Black)
                    }
                },
            )
        }
    }
    when (tabIndex) {
        0 -> {}
        1 -> {}
        2 -> {}
        3 -> {}
        4 -> {}
    }
}

@Composable
private fun IssuesScreen(paddingValues: PaddingValues) {
    val tabs = listOf("OPENED", "CLOSED")
    var tabIndex by remember { mutableStateOf(0) }

    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index, onClick = { tabIndex = index },
                text = {
                    if (tabIndex == index) {
                        androidx.compose.material3.Text(title, color = Color.Blue)
                    } else {
                        androidx.compose.material3.Text(title, color = Color.Black)
                    }
                },
            )
        }
    }
    when (tabIndex) {
        0 -> {}
        1 -> {}
    }
}

@Composable
private fun PullRequestsScreen(paddingValues: PaddingValues) {
    val tabs = listOf("OPENED", "CLOSED")
    var tabIndex by remember { mutableStateOf(0) }

    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index, onClick = { tabIndex = index },
                text = {
                    if (tabIndex == index) {
                        androidx.compose.material3.Text(title, color = Color.Blue)
                    } else {
                        androidx.compose.material3.Text(title, color = Color.Black)
                    }
                },
            )
        }
    }
    when (tabIndex) {
        0 -> {}
        1 -> {}
    }
}

@Composable
private fun ProjectsScreen(paddingValues: PaddingValues) {
    val tabs = listOf("OPENED", "CLOSED")
    var tabIndex by remember { mutableStateOf(0) }

    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth(1F)
            .padding(paddingValues)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index, onClick = { tabIndex = index },
                text = {
                    if (tabIndex == index) {
                        androidx.compose.material3.Text(title, color = Color.Blue)
                    } else {
                        androidx.compose.material3.Text(title, color = Color.Black)
                    }
                },
            )
        }
    }
    when (tabIndex) {
        0 -> {}
        1 -> {}
    }
}

@Composable
private fun BottomNav(onBottomBarClicked: (RepositoryScreens) -> Unit) {
    Surface(elevation = 16.dp) {
        BottomAppBar(containerColor = Color.White) {
            BottomNavigationItem(
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_code_24),
                        contentDescription = "Code Screen"
                    )
                },
                label = {
                    Text(
                        "Code",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                selected = false,
                onClick = {
                    onBottomBarClicked(RepositoryScreens.Code)
                },
            )

            BottomNavigationItem(
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                        contentDescription = "Issues Screen"
                    )
                },
                label = { Text("Issues") },
                selected = false,
                onClick = {
                    onBottomBarClicked(RepositoryScreens.Issues)
                },
            )

            BottomNavigationItem(
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                        contentDescription = "PullRequest Screen"
                    )
                },
                label = {
                    Text(
                        "Pull Requests",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        softWrap = false,
                    )
                },
                selected = false,
                onClick = {
                    onBottomBarClicked(RepositoryScreens.PullRequest)
                },
            )

            BottomNavigationItem(
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_project),
                        contentDescription = "PullRequest Screen"
                    )
                },
                label = { Text("Projects") },
                selected = false,
                onClick = {
                    onBottomBarClicked(RepositoryScreens.Projects)
                },
            )
        }
    }
}

@Composable
private fun Toolbar() {

    var showMenu by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }

    Row(verticalAlignment = Alignment.Top) {

        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(0.dp)
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = "Watch"
                )
            }
            Text(text = "180")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "Star"
                )
            }
            Text(text = "953")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fork),
                    contentDescription = "Star"
                )
            }
            Text(text = "75")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_book),
                    contentDescription = "Star"
                )
            }
            Text(text = "75")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pin),
                    contentDescription = "Pin"
                )
            }
            Text(text = "Pin")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_license),
                    contentDescription = "License"
                )
            }
            Text(text = "GPL-3")
        }

        Box{
            IconButton(
                onClick = {
                    showMenu = !showMenu
                },
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "more option")
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                //offset = pressOffset
            ) {
                DropdownMenuItem(text = { Text(text = "Share") }, onClick = { showMenu = false })
                DropdownMenuItem(
                    text = { Text(text = "Open in browser") },
                    onClick = { showMenu = false })
                DropdownMenuItem(text = { Text(text = "Copy URL") }, onClick = { showMenu = false })
            }
        }

    }
}

@Composable
private fun TitleHeader(
    //followersModelItem: FollowersModelItem, onItemClicked: (Int, String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

//            GlideImage(
//                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
//                imageModel = {
//                    followersModelItem.avatar_url
//                }, // loading a network image using an URL.
//                modifier = Modifier
//                    .size(48.dp, 48.dp)
//                    .size(48.dp, 48.dp)
//                    .clip(CircleShape),
//                imageOptions = ImageOptions(
//                    contentScale = ContentScale.Crop,
//                    alignment = Alignment.CenterStart,
//                    contentDescription = "Actor Avatar"
//                )
//            )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "JetBrains/kotlin",
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = Color.Black,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row {
                Text(text = "50 minutes ago", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "2.05 GB", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Kotlin", fontSize = 14.sp, color = Color.Yellow)
            }
        }

        Spacer(modifier = Modifier.weight(1F))

        IconButton(onClick = { /*TODO*/ }) {
            Icon(painter = painterResource(id = R.drawable.ic_label), contentDescription = "label")
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_info_outline),
                contentDescription = "info"
            )
        }
    }
}
