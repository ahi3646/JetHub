package com.hasan.jetfasthub.screens.main.repository

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.repository.models.repo_model.RepoModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.FileSizeCalculator
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFragment : Fragment() {

    private val repositoryViewModel: RepositoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        val owner = arguments?.getString("home_data") ?: ""
        val repo = arguments?.getString("home_extra") ?: ""

        Log.d("ahi3646", "onCreateView: $owner --- $repo")

        repositoryViewModel.getRepo(
            token = token,
            owner = owner,
            repo = repo
        )

        return ComposeView(requireContext()).apply {
            setContent {
                val state by repositoryViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state,
                        onBottomBarClicked = { repositoryScreen ->
                            repositoryViewModel.onBottomBarItemClicked(repositoryScreen)
                        },
                        onItemClicked = { dest, data ->
                            when (dest) {
                                -1 -> {
                                    findNavController().popBackStack()
                                }

                                else -> {
                                    val bundle = Bundle()
                                    bundle.putString("home_data", owner)
                                    findNavController().navigate(dest, bundle)
                                    Log.d("ahi3646", "onCreateView repo: $owner - - $data  ")
                                }
                            }
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContent(
    state: RepositoryScreenState,
    onBottomBarClicked: (RepositoryScreens) -> Unit,
    onItemClicked: (Int, String?) -> Unit
) {
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()



    BottomSheetScaffold(
        sheetContent = {
            if(state.repo.data!=null){
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.repo.data.full_name,
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.repo.data.description,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { scope.launch { sheetState.collapse() } }) {
                            Text(text = "OK")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        },
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) { sheetPadding ->
        Scaffold(
            modifier = Modifier.padding(sheetPadding),
            topBar = {
                Column(Modifier.fillMaxWidth()) {
                    TitleHeader(
                        state = state.repo,
                        onItemClicked = onItemClicked,
                        onAction = { action ->
                            when (action) {
                                "info" -> {
                                    scope.launch {
                                        if (sheetState.isCollapsed) {
                                            sheetState.expand()
                                        } else {
                                            sheetState.collapse()
                                        }
                                    }
                                }
                            }
                        }
                    )
                    Toolbar(state = state.repo, onItemClicked = onItemClicked)
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
private fun Toolbar(
    state: Resource<RepoModel>,
    onItemClicked: (Int, String?) -> Unit,
) {

    var showMenu by remember { mutableStateOf(false) }

    when (state) {
        is Resource.Loading -> {}
        is Resource.Success -> {
            val repository = state.data!!
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = { onItemClicked(-1, null) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
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
                        Text(text = repository.subscribers_count.toString())
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
                        Text(text = repository.stargazers_count.toString())
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
                        Text(text = repository.forks_count.toString())
                    }

                    if (repository.has_wiki) {
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
                            Text(text = "Wiki")
                        }
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

                    if (repository.license != null) {
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
                    }
                }

                Box {
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
                        DropdownMenuItem(
                            text = { Text(text = "Share") },
                            onClick = { showMenu = false })
                        DropdownMenuItem(
                            text = { Text(text = "Open in browser") },
                            onClick = { showMenu = false })
                        DropdownMenuItem(
                            text = { Text(text = "Copy URL") },
                            onClick = { showMenu = false })
                    }
                }

            }
        }

        is Resource.Failure -> {}
    }
}

@Composable
private fun TitleHeader(
    state: Resource<RepoModel>,
    onItemClicked: (Int, String?) -> Unit,
    onAction: (String) -> Unit
) {
    when (state) {
        is Resource.Loading -> {}
        is Resource.Success -> {

            val repository = state.data!!

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                GlideImage(
                    failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                    imageModel = {
                        repository.owner.avatar_url
                    }, // loading a network image using an URL.
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .size(48.dp, 48.dp)
                        .clip(CircleShape)
                        .clickable {
                            onItemClicked(
                                R.id.action_repositoryFragment_to_profileFragment,
                                repository.owner.login
                            )
                        },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.CenterStart,
                        contentDescription = "Actor Avatar"
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = repository.full_name,
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = Color.Black,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row {
                        Text(
                            text = ParseDateFormat.getTimeAgo(repository.updated_at).toString(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = FileSizeCalculator.humanReadableByteCountBin(repository.size.toLong()),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        if (repository.language != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = repository.language.toString(),
                                fontSize = 14.sp,
                                color = Color.Yellow
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1F))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_label),
                        contentDescription = "label"
                    )
                }

                IconButton(onClick = {
                    onAction("info")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info_outline),
                        contentDescription = "info"
                    )
                }
            }
        }

        is Resource.Failure -> {}
    }
}
