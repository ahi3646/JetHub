package com.hasan.jetfasthub.screens.main.search_files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme

class SearchFilesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                JetFastHubTheme {
                    MainContent(
                        onSearchClick = {

                        },
                        onBackPressed = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun MainContent(
    onSearchClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 10.dp,
                content = {
                    TopAppBarContent(
                        onBackPressed = onBackPressed, onSearchItemClick = onSearchClick
                    )
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {

        }
    }
}

@Composable
private fun TopAppBarContent(
    onSearchItemClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    var path by remember {
        mutableStateOf("In Paths")
    }
    var text by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButton(onClick = {
            onBackPressed()
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
        }

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = TextStyle(fontSize = 16.sp),
            label = null,
            placeholder = {
                Text(text = "Search", fontSize = 16.sp)
            },
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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            maxLines = 1,
            modifier = Modifier.weight(1F)
        )

        IconButton(
            onClick = {
                onSearchItemClick(text)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search icon"
            )
        }

        Row(
            Modifier
                .fillMaxHeight()
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = path, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dropdown_icon),
                    contentDescription = null
                )
            }
            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(text = "In Paths") },
                    onClick = {
                        path = "In Paths"
                        isExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "In Files") },
                    onClick = {
                        path = "In Files"
                        isExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "All") },
                    onClick = {
                        path = "All"
                        isExpanded = false
                    }
                )
            }
        }
    }
}
