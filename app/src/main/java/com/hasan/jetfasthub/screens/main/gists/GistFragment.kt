package com.hasan.jetfasthub.screens.main.gists

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.commits.CommitScreenSheets
import com.hasan.jetfasthub.screens.main.commits.models.commit_comments_model.CommitCommentsModelItem
import com.hasan.jetfasthub.screens.main.gists.gist_model.GistModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.FileSizeCalculator
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale
import kotlin.math.log

class GistFragment : Fragment() {

    private val gistViewModel: GistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        val gistId = arguments?.getString("gist_id")
        val gistOwner = arguments?.getString("gist_owner")

        if (gistId != null) {
            gistViewModel.getGist(token, gistId)
            gistViewModel.hasGistStarred(token, gistId)
        } else {
            Toast.makeText(requireContext(), "Can't find gist !", Toast.LENGTH_SHORT).show()
        }


        return ComposeView(requireContext()).apply {
            setContent {
                val state by gistViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onNavigate = { dest, data, id ->
                            when (dest) {
                                -1 -> {
                                    findNavController().popBackStack()
                                }

                                R.id.action_gistFragment_to_premiumFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_gistFragment_to_profileFragment -> {
                                    val bundle = bundleOf("home_date" to data)
                                    findNavController().navigate(
                                        dest, bundle
                                    )
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

                                "delete_gist" -> {
                                    gistViewModel.deleteGist(token, gistId!!)
                                        .flowWithLifecycle(
                                            lifecycle, Lifecycle.State.STARTED
                                        )
                                        .onEach { response ->
                                            if (response) {
                                                delay(2000)
                                                findNavController().popBackStack()
                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Can't delete a gist !",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        .launchIn(lifecycleScope)
                                }

                                "fork_gist" -> {
                                    gistViewModel.forkGist(token, data!!)
                                        .flowWithLifecycle(
                                            lifecycle, Lifecycle.State.STARTED
                                        )
                                        .onEach { response ->
                                            if (response) {

                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Can't fork a gist !",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        .launchIn(lifecycleScope)
                                }

                                "change_fork_status" -> {
                                    gistViewModel.changeForkStatus()
                                }
                            }
                        },
                        gistOwner = gistOwner!!
                    )
                }
            }
        }
    }

}


@Composable
private fun MainContent(
    state: GistScreenState,
    onNavigate: (Int, String?, Int?) -> Unit,
    onAction: (String, String?) -> Unit,
    gistOwner: String
) {
    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                TitleHeader(
                    state = state.Gist,
                    onNavigate = onNavigate,
                )
                Toolbar(
                    gistOwner = gistOwner,
                    state = state,
                    onNavigate = onNavigate,
                    onAction = onAction,
                    //                    onCurrentSheetChanged = {
//                        onCurrentSheetChanged(it)
//                        scope.launch {
//                            if (sheetScaffoldState.bottomSheetState.isCollapsed) {
//                                sheetScaffoldState.bottomSheetState.expand()
//                            } else {
//                                sheetScaffoldState.bottomSheetState.collapse()
//                            }
//                        }
//                    }
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
                    FilesScreen(
//                        state = state.Commit,
//                        onAction = onAction
                    )
                }

                1 -> CommentsScreen(
//                    state = state.CommitComments,
//                    onAction = onAction,
//                    onNavigate = onNavigate
                )
            }
        }
    }
}

@Composable
private fun FilesScreen(
//    state: Resource<CommitModel>,
//    onAction: (String, String?) -> Unit
) {
//    when (state) {
//        is Resource.Loading -> {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Loading ...")
//            }
//        }
//
//        is Resource.Success -> {
//            val files = state.data!!.files

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
//                items(files) { file ->
//                    ExpandableCard(file = file, onAction = onAction)
//                }
    }
//        }
//
//        is Resource.Failure -> {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Something went wrong !")
//            }
//        }
//    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CommentsScreen(
//    state: Resource<CommitCommentsModel>,
//    onAction: (String, String?) -> Unit,
//    onNavigate: (Int, String?, Int?) -> Unit
) {
//    when (state) {
//        is Resource.Loading -> {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Loading ...")
//            }
//        }
//
//        is Resource.Success -> {
//            val comments = state.data!!

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = ""
            )
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    //if (!comments.isEmpty()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
//                        itemsIndexed(comments) { index, comment ->
//                            CommentItem(
//                                comment = comment,
//                                onAction = onAction,
//                                onReply = { userLogin ->
//                                    if (userLogin != "N/A") {
//                                        //text = "@$userLogin"
//                                        val newValue = textFieldValueState.text.plus("@$userLogin")
//                                        textFieldValueState = TextFieldValue(
//                                            text = newValue,
//                                            selection = TextRange(newValue.length)
//                                        )
//                                        focusRequester.requestFocus()
//                                        keyboardController?.show()
//                                    } else {
//                                        Toast.makeText(
//                                            context,
//                                            "Can't identify user !",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                },
//                                onCurrentSheetChanged = onCurrentSheetChanged,
//                                onNavigate = onNavigate
//                            )
//                            if (index < comments.lastIndex) {
//                                Divider(
//                                    color = Color.Gray,
//                                    modifier = Modifier
//                                        .height(0.5.dp)
//                                        .padding(start = 6.dp, end = 6.dp)
//                                        .fillMaxWidth()
//                                        .align(Alignment.End)
//                                )
//                            }
//                        }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            androidx.compose.material3.TextField(
                value = textFieldValueState,
                onValueChange = {
                    textFieldValueState = it
                },
                textStyle = TextStyle(fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                maxLines = 1,
                modifier = Modifier
                    .weight(1F)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )

            IconButton(onClick = {
                textFieldValueState = TextFieldValue("")
                keyboardController?.hide()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    tint = Color.Blue,
                    contentDescription = "search icon"
                )
            }
        }
    }
    //} else {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(text = "No comments")
//                }
    //}
//        }
//
//        is Resource.Failure -> {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Something went wrong !")
//            }
//        }
//    }
}

//@Composable
//private fun CommentItem(
//    comment: CommitCommentsModelItem,
//    onAction: (String, String?) -> Unit,
//    onReply: (String) -> Unit,
//    onCurrentSheetChanged: (CommitScreenSheets) -> Unit,
//    onNavigate: (Int, String?, Int?) -> Unit
//) {
//
//    var showMenu by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//
//            GlideImage(
//                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
//                imageModel = {
//                    if (comment.user != null) {
//                        comment.user.avatar_url
//                    } else {
//                        R.drawable.baseline_account_circle_24
//                    }
//                    //repository.owner.avatar_url
//                }, // loading a network image using an URL.
//                modifier = Modifier
//                    .size(32.dp, 32.dp)
//                    .clip(CircleShape)
//                    .clickable { },
//                imageOptions = ImageOptions(
//                    contentScale = ContentScale.Crop,
//                    alignment = Alignment.CenterStart,
//                    contentDescription = "Actor Avatar"
//                )
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.weight(1F)
//            ) {
//                Row {
//                    Text(
//                        text = comment.user?.login ?: "N/A",
//                        fontSize = 14.sp,
//                        color = Color.Black
//                    )
//                    Spacer(modifier = Modifier.weight(1F))
//                    Text(
//                        text = ParseDateFormat.getTimeAgo(comment.created_at).toString(),
//                        fontSize = 12.sp,
//                        color = Color.Gray
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                if (comment.author_association.lowercase(Locale.ROOT) != "none") {
//                    Text(
//                        text = comment.author_association.lowercase(Locale.ROOT),
//                        color = Color.Gray,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//            }
//
//            IconButton(onClick = { }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_add_emoji),
//                    contentDescription = "emoji"
//                )
//            }
//
//            Box {
//                IconButton(
//                    onClick = {
//                        showMenu = !showMenu
//                    },
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_overflow),
//                        contentDescription = "more option"
//                    )
//                }
//
//                DropdownMenu(
//                    expanded = showMenu,
//                    onDismissRequest = { showMenu = false },
//                ) {
//                    DropdownMenuItem(
//                        text = { Text(text = "Edit") },
//                        onClick = {
//                            onNavigate(
//                                R.id.action_commitFragment_to_editCommentFragment,
//                                comment.body,
//                                comment.id
//                            )
//                            showMenu = false
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text(text = "Delete") },
//                        onClick = {
//                            onCurrentSheetChanged(
//                                CommitScreenSheets.CommitDeleteRequestSheet(
//                                    comment.id
//                                )
//                            )
//                            onAction("delete", "")
//                            showMenu = false
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text(text = "Reply") },
//                        onClick = {
//                            onReply(comment.user?.login ?: "N/A")
//                            showMenu = false
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text(text = "Share") },
//                        onClick = {
//                            onAction("share", comment.html_url)
//                            showMenu = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Text(
//            text = comment.body,
//            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, end = 8.dp)
//        )
//    }
//}

@Composable
private fun TitleHeader(
    state: Resource<GistModel>,
    onNavigate: (Int, String?, Int?) -> Unit,
) {
    when (state) {

        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(
                            text = "",
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                    }
                }
            }
        }

        is Resource.Success -> {

            val gist = state.data!!

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    GlideImage(
                        failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                        imageModel = {
                            if (gist.owner != null) {
                                gist.owner.avatar_url
                            } else {
                                R.drawable.baseline_account_circle_24
                            }
                        }, // loading a network image using an URL.
                        modifier = Modifier
                            .size(48.dp, 48.dp)
                            .size(48.dp, 48.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (gist.owner != null) {
                                    onNavigate(
                                        R.id.action_commitFragment_to_profileFragment,
                                        gist.owner.login,
                                        null
                                    )
                                }
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
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(gist.owner?.login ?: "N/A")
                                }
                                append(" / ")
                                append(gist.description)

                            },
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text(
                                text = ParseDateFormat.getTimeAgo(gist.created_at).toString(),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            if (gist.history.size != 1) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "edited",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }

                            val file = gist.files.values
                            val fileSize = file.elementAt(0).size

                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = FileSizeCalculator.humanReadableByteCountBin(fileSize.toLong()),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(
                            text = "",
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                    }
                }
            }
        }
    }
}

@Composable
private fun Toolbar(
    gistOwner: String,
    state: GistScreenState,
    onNavigate: (Int, String?, Int?) -> Unit,
    onAction: (String, String?) -> Unit,
) {
    when (state.Gist) {
        is Resource.Loading -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {

                IconButton(
                    onClick = {
                        onNavigate(-1, null, null)
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Text(
                    text = "Gist",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Row(
                    Modifier.weight(1F),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {

                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_browser),
                            contentDescription = "browser incon"
                        )
                    }

                    IconButton(onClick = {
                        onNavigate(R.id.action_gistFragment_to_premiumFragment, null, null)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = "pin incon"
                        )
                    }

                }


                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "more option")
                }

            }
        }

        is Resource.Success -> {
            var showMenu by remember { mutableStateOf(false) }
            val gist = state.Gist.data!!

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {

                IconButton(
                    onClick = {
                        onNavigate(-1, null, null)
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Text(
                    text = "Gist",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Row(
                    Modifier.weight(1F),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {

                    if (gist.owner.login == gistOwner) {
                        IconButton(onClick = {
                            onNavigate(R.id.action_gistFragment_to_premiumFragment, null, null)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null
                            )
                        }
                    }

                    if (gist.owner.login != gistOwner) {
                        IconButton(onClick = {
                            if (state.HasForked) {
                                onAction("change_fork_status", null)
                            } else {
                                onAction("fork_gist", gist.id)
                            }
                        }) {
                            if (state.HasForked) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fork),
                                    contentDescription = null,
                                    tint = Color.Blue
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fork),
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                    IconButton(onClick = {
                        onAction("start_gist", gist.id)
                    }) {
                        Log.d("ahi3646", "Toolbar: ${state.HasGistStarred} ")
                        if (state.HasGistStarred) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_filled),
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = null
                            )
                        }
                    }

                    IconButton(onClick = {
                        onAction("browser", gist.html_url)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_browser),
                            contentDescription = "browser incon"
                        )
                    }

                    IconButton(onClick = {
                        onNavigate(R.id.action_gistFragment_to_premiumFragment, null, null)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = "pin incon"
                        )
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
                        DropdownMenuItem(
                            text = { Text(text = "Share") },
                            onClick = {
                                onAction("share", gist.html_url)
                                showMenu = false
                            }
                        )

                        if (gist.owner.login == gistOwner) {
                            DropdownMenuItem(
                                text = { Text(text = "Delete") },
                                onClick = {
                                    onAction("delete_gist", gist.id)
                                    showMenu = false
                                }
                            )
                        }

                    }
                }

            }
        }

        is Resource.Failure -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {

                IconButton(
                    onClick = {
                        onNavigate(-1, null, null)
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back button")
                }

                Text(
                    text = "Gist",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Row(
                    Modifier.weight(1F),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {

                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_browser),
                            contentDescription = "browser incon"
                        )
                    }

                    IconButton(onClick = {
                        onNavigate(R.id.action_gistFragment_to_premiumFragment, null, null)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = "pin incon"
                        )
                    }

                }


                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "more option")
                }

            }
        }
    }
}

@Preview
@Composable
private fun GistFileItem() {
    Surface() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(text = "here should be gist file name . extension")
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                color = Color.Gray
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                Text(text = "Kotlin")
                Spacer(modifier = Modifier.weight(1F))
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxHeight()  //fill the max height
                        .width(1.dp)
                )
                Text(text = "16KB", color = Color.Gray)
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxHeight()  //fill the max height
                        .width(1.dp)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GistCommentItem() {

    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

//            GlideImage(
//                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
//                imageModel = {
//                    if (comment.user != null) {
//                        comment.user.avatar_url
//                    } else {
//                        R.drawable.baseline_account_circle_24
//                    }
//                    //repository.owner.avatar_url
//                }, // loading a network image using an URL.
//                modifier = Modifier
//                    .size(32.dp, 32.dp)
//                    .clip(CircleShape)
//                    .clickable { },
//                imageOptions = ImageOptions(
//                    contentScale = ContentScale.Crop,
//                    alignment = Alignment.CenterStart,
//                    contentDescription = "Actor Avatar"
//                )
//            )

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

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = "Hasan Anorov",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = "Yesterday",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Box {
                IconButton(
                    onClick = {
                        showMenu = !showMenu
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_overflow),
                        contentDescription = "more option"
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        onClick = {
//                            onNavigate(R.id.action_commitFragment_to_editCommentFragment, comment.body, comment.id)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        onClick = {
//                            onCurrentSheetChanged(
//                                CommitScreenSheets.CommitDeleteRequestSheet(
//                                    comment.id
//                                )
//                            )
                            //onAction("delete", "")
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Reply") },
                        onClick = {
                            //onReply(comment.user?.login ?: "N/A")
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Share") },
                        onClick = {
                            //onAction("share", comment.html_url)
                            showMenu = false
                        }
                    )
                }
            }
        }

        Text(
            text = "here should be message body",
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, end = 8.dp)
        )
    }
}