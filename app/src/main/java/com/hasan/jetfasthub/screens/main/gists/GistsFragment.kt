package com.hasan.jetfasthub.screens.main.gists

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.profile.model.gist_model.GistModel
import com.hasan.jetfasthub.screens.main.profile.model.gist_model.GistModelItem
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class GistsFragment : Fragment() {

    private val gistsViewModel: GistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        val username = "HasanAnorov"

        gistsViewModel.getUserGists(token = token, username = username, 1)

        return ComposeView(requireContext()).apply {
            setContent {
                val state by gistsViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onNavigate = { dest, data ->
                            Log.d("ahi3646", "onCreateView: $dest , $data ")
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun MainContent(state: GistsScreenState, onNavigate: (String, String?) -> Unit) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBarContent() }
    ) { paddingValues ->
        TabScreen(contentPaddingValues = paddingValues, state, onNavigate)
    }
}

@Composable
fun TabScreen(
    contentPaddingValues: PaddingValues,
    state: GistsScreenState,
    onRecyclerItemClick: (String, String?) -> Unit
) {

    var tabIndex by remember { mutableStateOf(0) }
    val tabs =
        listOf("MY GISTS", "STARRED", "PUBLIC GISTS")

    Column(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxWidth()
    ) {
        androidx.compose.material.TabRow(
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
            0 -> MyGists(state.UserGists, onRecyclerItemClick)
            1 -> Starred()
            2 -> PublicGists()
        }
    }
}

@Composable
fun MyGists(state: Resource<GistModel>, onRecyclerItemClick: (String, String?) -> Unit) {
    when (state) {
        is Resource.Loading -> {
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

        is Resource.Success -> {
            if (!state.data!!.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    itemsIndexed(state.data) { index, gist ->
                        GistItemCard(
                            gist, onGistItemClick = onRecyclerItemClick
                        )
                        if (index < state.data.lastIndex) {
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No news",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Something went wrong !")
                Log.d("ahi3646", "Unread: ${state.errorMessage}")
            }
        }
    }
}

@Composable
fun GistItemCard(
    gistModelItem: GistModelItem, onGistItemClick: (String, String?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onGistItemClick("enter_dest", gistModelItem.url)
            })
            .padding(4.dp), elevation = 0.dp, backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)

        ) {

            androidx.compose.material.Text(
                text = gistModelItem.description,
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = Color.Black,
                fontSize = 18.sp,
                style = androidx.compose.material.MaterialTheme.typography.subtitle2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material.Text(
                text = ParseDateFormat.getTimeAgo(gistModelItem.updated_at).toString(),
                color = Color.Black,
                modifier = Modifier.padding(start = 2.dp)
            )

        }
    }
}

@Composable
fun Starred() {

}

@Composable
fun PublicGists() {

}

@Composable
private fun TopAppBarContent(
    //onBackPressed: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
        ) {
            IconButton(onClick = {
                //onBackPressed(R.id.action_aboutFragment_to_homeFragment)
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
            }

            Text(
                color = Color.Black,
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 10.dp, end = 10.dp),
                text = "Gists",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
