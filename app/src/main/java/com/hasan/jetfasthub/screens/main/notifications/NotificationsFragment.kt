package com.hasan.jetfasthub.screens.main.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationsFragment : Fragment() {

    private val notificationsViewModel: NotificationsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        Log.d("ahi3646", "onCreateView: token - $token")

        notificationsViewModel.getAllNotifications(token)

        return ComposeView(requireContext()).apply {
            setContent {
                val state by notificationsViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onNavigate = { dest -> findNavController().navigate(dest) })
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    state: NotificationsScreenState,
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
        TabScreen(contentPadding, notifications = state.allNotifications)
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
            onBackPressed(R.id.action_notificationsFragment_to_homeFragment)
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
        }

        Text(
            color = Color.Black,
            modifier = Modifier
                .weight(1F)
                .padding(start = 10.dp, end = 10.dp),
            text = "Notifications",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun TabScreen(contentPaddingValues: PaddingValues, notifications: AllNotifications) {

    var tabIndex by remember { mutableStateOf(0) }
    val tabs =
        listOf("UNREAD", "ALL", "JETHUB")

    Column(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxWidth()
    ) {
        TabRow(selectedTabIndex = tabIndex, containerColor = Color.White) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }
        when (tabIndex) {
            0 -> Unread(notifications)
            1 -> All(notifications)
            2 -> JetHub(notifications)
        }
    }
}

@Composable
fun Unread(unreadNotifications: AllNotifications) {
    when (unreadNotifications) {
        is AllNotifications.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...")
            }
        }

        is AllNotifications.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Success ...")
            }
        }

        is AllNotifications.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
                Log.d("ahi3646", "Unread: ${unreadNotifications.errorMessage}")
            }
        }
    }
}

@Composable
fun All(allNotifications: AllNotifications) {
    when (allNotifications) {
        is AllNotifications.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...")
            }
        }

        is AllNotifications.Success -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

            }
        }

        is AllNotifications.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
                Log.d("ahi3646", "Unread: ${allNotifications.errorMessage}")
            }
        }
    }
}

@Composable
fun JetHub(jetHubNotifications: AllNotifications) {
    when (jetHubNotifications) {
        is AllNotifications.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...")
            }
        }

        is AllNotifications.Success -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

            }
        }

        is AllNotifications.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
                Log.d("ahi3646", "Unread: ${jetHubNotifications.errorMessage}")
            }
        }
    }
}