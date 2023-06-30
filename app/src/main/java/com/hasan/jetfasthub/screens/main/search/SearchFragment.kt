package com.hasan.jetfasthub.screens.main.search

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(requireContext()).apply {
            setContent {
                JetFastHubTheme {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 0.dp,
                content = {
                    TopAppBarContent()
                },
            )
        },

        ) { contentPadding ->
        TabScreen(contentPadding)
    }
}

@Composable
fun TabScreen(contentPaddingValues: PaddingValues) {
    val tabs = listOf("REPOSITORIES", "USERS", "ISSUES", "CODE")
    var tabIndex by remember { mutableStateOf(0) }
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
            0 -> RepositoriesContent()
            1 -> UsersContent()
            2 -> IssuesContent()
            3 -> CodeContent()
        }
    }
}

@Composable
fun RepositoriesContent() {

}

@Composable
fun UsersContent() {

}

@Composable
fun IssuesContent() {

}

@Composable
fun CodeContent() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            //onBackPressed(R.id.action_searchFragment_to_homeFragment)
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
        }

        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = TextStyle(fontSize = 16.sp),
            label = null,
            placeholder = { Text(text = "Search", fontSize = 16.sp) },
            trailingIcon = {
                if (text != "") {
                    IconButton(onClick = { text = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "clear text icon"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            maxLines = 1,
            modifier = Modifier.weight(1F)
        )

        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search icon"
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainContent() {
    MainContent()
}

