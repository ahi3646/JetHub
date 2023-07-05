package com.hasan.jetfasthub.screens.main.organisations

import android.os.Bundle
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrganisationsFragment: Fragment() {

    private val organisationsViewModel: OrganisationsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val organisation = arguments?.getString("profile_data") ?: ""
        val token = PreferenceHelper.getToken(requireContext())

        return ComposeView(requireContext()).apply {
           setContent {
               JetFastHubTheme {
                   MainContent()
               }
           }
        }
    }

}

@Preview
@Composable
fun PreviewMainContent(){
    MainContent()
}

@Composable
private fun MainContent(){
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        content = {
            TopAppBarContent()
        },
    )
    }) { paddingValues ->
        TabScreen(paddingValues)
    }
}

@Composable
private fun TabScreen(paddingValues: PaddingValues){

    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("OVERVIEW", "REPOSITORIES", "PEOPLE")

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
    ) {

        ScrollableTabRow(selectedTabIndex = tabIndex, containerColor = Color.White ) {
            tabs.forEachIndexed{ index, title ->
                Tab(
                    text = {
                        if (tabIndex == index) {
                            androidx.compose.material3.Text(title, color = Color.Blue)
                        } else {
                            androidx.compose.material3.Text(title, color = Color.Black)
                        }
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }

        when(tabIndex){
            0 -> {}
            1 -> {}
            2 -> {}
        }
    }
}

@Composable
private fun TopAppBarContent(
   // onBackPressed: (Int) -> Unit, username: String
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            //onBackPressed(R.id.action_profileFragment_to_homeFragment)
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
        }

        Text(
            color = Color.Black,
            modifier = Modifier
                .weight(1F)
                .padding(start = 10.dp, end = 10.dp),
            text = "Enter your organisation",
            style = MaterialTheme.typography.titleLarge,
        )

        IconButton(onClick = { }) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }
    }
}