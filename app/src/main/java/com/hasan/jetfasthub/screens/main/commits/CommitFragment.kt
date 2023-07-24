package com.hasan.jetfasthub.screens.main.commits

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.CommitModel
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.File
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommitFragment : Fragment() {

    private val commitViewModel: CommitViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        val owner = arguments?.getString("owner", "")
        val repo = arguments?.getString("repo", "")
        val sha = arguments?.getString("sha", "")

        if (!owner.isNullOrEmpty() && !repo.isNullOrEmpty() && !sha.isNullOrEmpty()) {
            commitViewModel.getCommit(
                token = token,
                owner = owner,
                repo = repo,
                branch = sha
            )
        } else {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        return ComposeView(requireContext()).apply {
            setContent {
                val state by commitViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        owner = owner!!,
                        repo = repo!!,
                        state = state,
                        onNavigate = { dest, data ->
                            when (dest) {
                                -1 -> {
                                    findNavController().popBackStack()
                                }

                                R.id.action_commitFragment_to_profileFragment -> {
                                    val bundle = bundleOf("home_data" to data)
                                    findNavController().navigate(dest, bundle)
                                }
                            }
                        },
                        onAction = { action, data ->
                            when (action) {
                                "share" -> {
                                    val context = requireContext()
                                    val type = "text/plain"
                                    val subject = "Your subject"
                                    val shareWith = "ShareWith"

                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = type
                                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                                    intent.putExtra(Intent.EXTRA_TEXT, data)

                                    ContextCompat.startActivity(
                                        context, Intent.createChooser(intent, shareWith), null
                                    )
                                }

                                "browser" -> {
                                    var webpage = Uri.parse(data)

                                    if (!data!!.startsWith("http://") && !data.startsWith("https://")) {
                                        webpage = Uri.parse("http://$data")
                                    }
                                    val urlIntent = Intent(
                                        Intent.ACTION_VIEW, webpage
                                    )
                                    requireContext().startActivity(urlIntent)
                                }

                                "copy" -> {
                                    val clipboardManager =
                                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clipData = ClipData.newPlainText("text", data)
                                    clipboardManager.setPrimaryClip(clipData)
                                    Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT)
                                        .show()
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
    owner: String,
    repo: String,
    state: CommitScreenState,
    onNavigate: (Int, String?) -> Unit,
    onAction: (String, String?) -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        //animationSpec = spring(dampingRatio = Spring.DefaultDisplacementThreshold)
    )
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val closeSheet: () -> Unit = {
        scope.launch {
            sheetState.collapse()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                scope.launch {
                    if (sheetScaffoldState.bottomSheetState.isExpanded) {
                        sheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            })
        },
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            when(state.Commit){
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val commit = state.Commit.data!!
                    Column(
                        Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = commit.commit.message, style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "$owner / $repo"
                        )

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                closeSheet()
                            }) {
                                Text(text = "Cancel", color = Color.Red)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(onClick = {
                                closeSheet()
                            }) {
                                Text(text = "OK", color = Color.Blue)
                            }
                        }
                    }
                }
                is Resource.Failure -> {}
            }
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {

    }

    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                TitleHeader(
                    repo = repo,
                    state = state.Commit,
                    onNavigate = onNavigate,
                    onCurrentSheetChanged = {
                        scope.launch {
                            if (sheetScaffoldState.bottomSheetState.isCollapsed) {
                                sheetScaffoldState.bottomSheetState.expand()
                            } else {
                                sheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                )
                Toolbar(
                    state = state.Commit,
                    onNavigate = onNavigate,
                    onAction = onAction,
                )
            }
        },
    ) { paddingValues ->

        var tabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf("FILES", "COMMENTS")

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index, onClick = { tabIndex = index },
                        text = {
                            if (tabIndex == index) {
                                Text(title, color = Color.Blue)
                            } else {
                                Text(title, color = Color.Black)
                            }
                        },
                    )
                }
            }

            when (tabIndex) {
                0 -> {
                    FilesScreen(state.Commit)
                }

                1 -> CommentsScreen()
            }
        }
    }
}

@Composable
private fun FilesScreen(
    state: Resource<CommitModel>
) {
    when (state) {
        is Resource.Loading -> {

        }

        is Resource.Success -> {
            val files = state.data!!.files

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                items(files) { file ->
                    CommentItemCard(file)
                }
            }
        }

        is Resource.Failure -> {

        }
    }
}

@Composable
private fun CommentItemCard(file: File) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { }),
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        Column {

            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Text(text = file.filename)

                Spacer(Modifier.weight(1F))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dropdown_icon),
                        contentDescription = "dropdown icon"
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "options menu")
                }
            }

            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .height(0.5.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Changes")
                    Text(text = file.changes.toString())
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Addition")
                    Text(text = file.additions.toString())
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Deletion")
                    Text(text = file.deletions.toString())
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Status")
                    Text(text = file.status, fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}

@Composable
private fun CommentsScreen() {

}

@Composable
private fun TitleHeader(
    repo: String,
    state: Resource<CommitModel>,
    onNavigate: (Int, String?) -> Unit,
    onCurrentSheetChanged: () -> Unit
) {
    when (state) {

        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        contentDescription = "avatar icon",
                        modifier = Modifier
                            .size(48.dp, 48.dp)
                            .size(48.dp, 48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.height(4.dp))

                        Row {

                            Spacer(modifier = Modifier.width(6.dp))

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        is Resource.Success -> {
            val commit = state.data!!

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    GlideImage(
                        failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                        imageModel = {
                            if (commit.author != null) {
                                commit.author.avatar_url
                            } else {
                                R.drawable.baseline_account_circle_24
                            }
                            //repository.owner.avatar_url
                        }, // loading a network image using an URL.
                        modifier = Modifier
                            .size(48.dp, 48.dp)
                            .size(48.dp, 48.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (commit.author != null) {
                                    onNavigate(
                                        R.id.action_commitFragment_to_profileFragment,
                                        commit.author.login
                                    )
                                }
                            },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.CenterStart,
                            contentDescription = "Actor Avatar"
                        )
                    )

//                    Image(
//                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
//                        contentDescription = "avatar icon",
//                        modifier = Modifier
//                            .size(48.dp, 48.dp)
//                            .size(48.dp, 48.dp)
//                            .clip(CircleShape),
//                        contentScale = ContentScale.Crop,
//                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = commit.commit.message,
                            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text(
                                text = repo,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = ParseDateFormat.getTimeAgo(commit.commit.committer.date)
                                    .toString(),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }


                    IconButton(onClick = {
                        onCurrentSheetChanged()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info_outline),
                            contentDescription = "info"
                        )
                    }
                }

            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        contentDescription = "avatar icon",
                        modifier = Modifier
                            .size(48.dp, 48.dp)
                            .size(48.dp, 48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.height(4.dp))

                        Row {

                            Spacer(modifier = Modifier.width(6.dp))

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

    }
}

@Composable
private fun Toolbar(
    state: Resource<CommitModel>,
    onNavigate: (Int, String?) -> Unit,
    onAction: (String, String?) -> Unit,
) {

    when (state) {
        is Resource.Loading -> {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 4.dp)
            ) {

                IconButton(onClick = { }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_document),
                            contentDescription = "changes",
                            tint = Color.Black
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add",
                        )
                    }


                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "clear"
                        )
                    }

                }

                IconButton(
                    onClick = { },
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "more option")
                }
            }
        }

        is Resource.Success -> {
            var showMenu by remember { mutableStateOf(false) }

            val commit = state.data!!

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {

                IconButton(onClick = { onNavigate(-1, null) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_document),
                            contentDescription = "changes",
                            tint = Color.Black
                        )
                        Text(text = commit.files.size.toString())
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add",
                        )
                        Text(text = commit.stats.additions.toString())
                    }


                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "clear"
                        )
                        Text(text = commit.stats.deletions.toString())
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
                    ) {
                        DropdownMenuItem(text = { Text(text = "Share") }, onClick = {
                            onAction("share", "repository.html_url")
                            showMenu = false
                        })
                        DropdownMenuItem(text = { Text(text = "Open in browser") }, onClick = {
                            onAction("browser", "repository.html_url")
                            showMenu = false
                        })
                        DropdownMenuItem(text = { Text(text = "Copy URL") }, onClick = {
                            onAction("copy", "repository.html_url")
                            showMenu = false
                        })

                        DropdownMenuItem(text = { Text(text = "Copy SHA-1") },
                            onClick = {
                                onAction("copy", "copy sha-1 ")
                                showMenu = false
                            })

                    }
                }

            }
        }

        is Resource.Failure -> {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 4.dp)
            ) {

                IconButton(onClick = { }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_document),
                            contentDescription = "changes",
                            tint = Color.Black
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add",
                        )
                    }


                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "clear"
                        )
                    }

                }

                IconButton(
                    onClick = { },
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "more option")
                }
            }
        }
    }
}
