package com.hasan.jetfasthub.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {

                val navController = rememberNavController()

                JetFastHubTheme {
                    MainContent(navController)
                }
            }
        }
    }

    @Composable
    fun MainContent(navController: NavHostController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    content = {
                        TopAppBarContent()
                    },
                )
            },
            bottomBar = {
                BottomNav(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    navController
                )
            },

            ) { contentPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.Feeds.route,
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(AppScreens.Feeds.route) {
                    FeedsScreen()
                }
                composable(AppScreens.Issues.route) {
                    IssuesScreen()
                }
                composable(AppScreens.PullRequests.route) {
                    PullRequestScreen()
                }
            }
        }
    }

    @Composable
    fun FeedsScreen(){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Feeds Screen")
        }
    }

    @Composable
    fun IssuesScreen(){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Issues Screen")
        }
    }

    @Composable
    fun PullRequestScreen(){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "PullScreen Screen")
        }
    }

    @Composable
    fun BottomNav(modifier: Modifier, navController: NavHostController) {
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
                        onClick = {
                            navController.navigate(AppScreens.Feeds.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
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
                        onClick = {
                            if (navController.currentDestination?.route != AppScreens.Issues.route)
                            navController.navigate(AppScreens.Issues.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
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
                        onClick = {
                            navController.navigate(AppScreens.PullRequests.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun TopAppBarContent() {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { }) {
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

}
