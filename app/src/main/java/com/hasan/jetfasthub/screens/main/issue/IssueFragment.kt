package com.hasan.jetfasthub.screens.main.issue

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.commits.CommitScreenSheets

import com.hasan.jetfasthub.screens.main.commits.models.commit_comments_model.CommitCommentsModel
import com.hasan.jetfasthub.screens.main.commits.models.commit_comments_model.CommitCommentsModelItem
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.CommitModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import com.hasan.jetfasthub.utility.ParseDateFormat
import com.hasan.jetfasthub.utility.Resource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class IssueFragment : Fragment() {

    private val issueViewModel: IssueViewModel by viewModel()
    private lateinit var token: String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        token = PreferenceHelper.getToken(requireContext())

        val owner = arguments?.getString("issue_owner")
        val repo = arguments?.getString("issue_repo")
        val issueNumber = arguments?.getString("issue_number")

        if (issueNumber != null && owner != null && repo != null) {
            issueViewModel.getIssue(token, owner, repo, issueNumber)
        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                JetFastHubTheme {
                    val state = issueViewModel.state.collectAsState()
                    MainContent()
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Preview()
@Composable
private fun MainContent() {

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
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

        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface
    ) { sheetPadding ->
        Scaffold(
            modifier = Modifier.padding(sheetPadding),
            topBar = {
                Column(Modifier.fillMaxWidth()) {
                    TitleHeader(
//                        repo = state.CommitRepo,
//                        state = state.Commit,
                        onNavigate = { dest, data, extra ->

                        },
                        onCurrentSheetChanged = {
//                            onCurrentSheetChanged(it)
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
//                        state = state.Commit,
                        onNavigate = { dest, data, extra ->

                        },
                        onAction = { action, data -> },
                    )
                }
            },
        ) { paddingValues ->
            CommentsScreen(
                paddingValues
                //state = state.CommitComments,
//                onNavigate = onNavigate,
//                onAction = onAction,
//                onCurrentSheetChanged = {
//                    onCurrentSheetChanged(it)
//                    scope.launch {
//                        if (sheetScaffoldState.bottomSheetState.isCollapsed) {
//                            sheetScaffoldState.bottomSheetState.expand()
//                        } else {
//                            sheetScaffoldState.bottomSheetState.collapse()
//                        }
//                    }
//                }
            )

        }
    }

}

@Composable
private fun TitleHeader(
    onNavigate: (Int, String?, Int?) -> Unit,
    onCurrentSheetChanged: () -> Unit
) {
//    when (state) {
//
//        is Resource.Loading -> {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//
//                    Image(
//                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
//                        contentDescription = "avatar icon",
//                        modifier = Modifier
//                            .size(48.dp, 48.dp)
//                            .size(48.dp, 48.dp)
//                            .clip(CircleShape),
//                        contentScale = ContentScale.Crop,
//                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
//                    )
//
//                    Spacer(
//                        modifier = Modifier
//                            .width(24.dp)
//                            .width(6.dp)
//                    )
//                }
//            }
//        }
//
//        is Resource.Success -> {
//            val commit = state.data!!

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

//                    GlideImage(
//                        failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
//                        imageModel = {
//                            if (commit.author != null) {
//                                commit.author.avatar_url
//                            } else {
//                                R.drawable.baseline_account_circle_24
//                            }
//                        }, // loading a network image using an URL.
//                        modifier = Modifier
//                            .size(48.dp, 48.dp)
//                            .size(48.dp, 48.dp)
//                            .clip(CircleShape)
//                            .clickable {
//                                if (commit.author != null) {
//                                    onNavigate(
//                                        R.id.action_commitFragment_to_profileFragment,
//                                        commit.author.login,
//                                        null
//                                    )
//                                }
//                            },
//                        imageOptions = ImageOptions(
//                            contentScale = ContentScale.Crop,
//                            alignment = Alignment.CenterStart,
//                            contentDescription = "Actor Avatar"
//                        )
//                    )

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
                    text = "commit.commit.message",
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "repo",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tuesday",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            IconButton(
                onClick = {
                    onCurrentSheetChanged()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info_outline),
                    contentDescription = "info",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        }
    }
//        }
//
//        is Resource.Failure -> {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surface)
//                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//
//                    Image(
//                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
//                        contentDescription = "avatar icon",
//                        modifier = Modifier
//                            .size(48.dp, 48.dp)
//                            .size(48.dp, 48.dp)
//                            .clip(CircleShape),
//                        contentScale = ContentScale.Crop,
//                    )
//
//                    Spacer(modifier = Modifier.width(12.dp))
//
//                    Column(
//                        horizontalAlignment = Alignment.Start,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//
//                        Spacer(modifier = Modifier.height(4.dp))
//
//                        Row {
//
//                            Spacer(modifier = Modifier.width(6.dp))
//
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            }
//        }
//
//    }
}

@Composable
private fun Toolbar(
    //state: Resource<CommitModel>,
    onNavigate: (Int, String?, Int?) -> Unit,
    onAction: (String, String?) -> Unit,
) {
//    when (state) {
//        is Resource.Loading -> {
//            Row(
//                verticalAlignment = Alignment.Top,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surface)
//                    .padding(top = 4.dp)
//            ) {
//
//                IconButton(onClick = { }) {
//                    Icon(
//                        Icons.Filled.ArrowBack,
//                        contentDescription = "Back button",
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(4.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_document),
//                            contentDescription = "changes",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(4.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_add),
//                            contentDescription = "add",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(4.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_clear),
//                            contentDescription = "clear",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//
//                }
//
//                IconButton(
//                    onClick = { },
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.MoreVert,
//                        contentDescription = "more option",
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//        }
//
//        is Resource.Success -> {
    var showMenu by remember { mutableStateOf(false) }

//            val commit = state.data!!

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {

        IconButton(onClick = { onNavigate(-1, null, null) }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1F)
        ) {
            Text(text = "#240", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "youtubedl-android",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Back button",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = "Back button",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Box {
            IconButton(
                onClick = {
                    showMenu = !showMenu
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "more option",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(text = {
                    Text(
                        text = "Share",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }, onClick = {
                    onAction("share", "commit.html_url")
                    showMenu = false
                })
                DropdownMenuItem(text = {
                    Text(
                        text = "Open in browser",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }, onClick = {
                    onAction("browser", "commit.html_url")
                    showMenu = false
                })
            }
        }

    }
//        }
//
//        is Resource.Failure -> {
//            Row(
//                verticalAlignment = Alignment.Top,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surface)
//                    .padding(top = 4.dp)
//            ) {
//
//                IconButton(onClick = { }) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = "Back button",
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(4.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_document),
//                            contentDescription = "changes",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(4.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_add),
//                            contentDescription = "add",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(4.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_clear),
//                            contentDescription = "clear",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//
//                }
//
//                IconButton(
//                    onClick = { },
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.MoreVert,
//                        contentDescription = "more option",
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//        }
//    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CommentsScreen(
    contentPaddingValues: PaddingValues
//    state: Resource<CommitCommentsModel>,
//    onAction: (String, String?) -> Unit,
//    onCurrentSheetChanged: (CommitScreenSheets) -> Unit,
//    onNavigate: (Int, String?, Int?) -> Unit
) {
//    when (state) {
//
//        is Resource.Loading -> {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.surfaceVariant),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    val context = LocalContext.current

//            if (!comments.isEmpty()) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.surfaceVariant),
//                ) {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1F)
//                            .background(MaterialTheme.colorScheme.surfaceVariant)
//                    ) {
//                        itemsIndexed(comments) { index, comment ->
//                            CommentItem(
//                                comment = comment,
//                                onAction = onAction,
//                                onReply = { userLogin ->
//                                    if (userLogin != "N/A") {
//                                        val newValue = textFieldValueState.text.plus("@$userLogin")
//                                        textFieldValueState = TextFieldValue(
//                                            text = newValue, selection = TextRange(newValue.length)
//                                        )
//                                        focusRequester.requestFocus()
//                                        keyboardController?.show()
//                                    } else {
//                                        Toast.makeText(
//                                            context, "Can't identify user !", Toast.LENGTH_SHORT
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
//                    }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color.LightGray),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        TextField(
//                            value = textFieldValueState,
//                            onValueChange = {
//                                textFieldValueState = it
//                            },
//                            textStyle = TextStyle(fontSize = 16.sp),
//                            colors = TextFieldDefaults.colors(
//                                focusedContainerColor = Color.Transparent,
//                                unfocusedContainerColor = Color.Transparent,
//                                disabledContainerColor = Color.Transparent,
//                                focusedIndicatorColor = Color.Transparent,
//                                unfocusedIndicatorColor = Color.Transparent,
//                            ),
//                            maxLines = 1,
//                            modifier = Modifier
//                                .weight(1F)
//                                .focusRequester(focusRequester),
//                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
//                        )
//
//                        IconButton(onClick = {
//                            if (textFieldValueState.text.length >= 2) {
//                                onAction(
//                                    "post_comment", textFieldValueState.text
//                                )
//                                textFieldValueState = TextFieldValue("")
//                                keyboardController?.hide()
//                            } else {
//                                Toast.makeText(
//                                    context, "Min length for comment is 2 !", Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_send),
//                                tint = Color.Blue,
//                                contentDescription = "search icon"
//                            )
//                        }
//                    }
//                }
//            } else {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPaddingValues)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No comments",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
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
                if (textFieldValueState.text.length >= 2) {
//                                onAction(
//                                    "post_comment", textFieldValueState.text
//                                )
                    textFieldValueState = TextFieldValue("")
                    keyboardController?.hide()
                } else {
                    Toast.makeText(
                        context, "Min length for comment is 2 !", Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    tint = Color.Blue,
                    contentDescription = "search icon"
                )
            }
        }
    }
//            }
//        }

//        is Resource.Failure -> {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.surfaceVariant),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "Can't load data!",
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
}

@Composable
private fun CommentItem(
    comment: CommitCommentsModelItem,
    onAction: (String, String?) -> Unit,
    onReply: (String) -> Unit,
    onCurrentSheetChanged: (CommitScreenSheets) -> Unit,
    onNavigate: (Int, String?, Int?) -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            GlideImage(
                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                imageModel = {
                    if (comment.user != null) {
                        comment.user.avatar_url
                    } else {
                        R.drawable.baseline_account_circle_24
                    }
                    //repository.owner.avatar_url
                }, // loading a network image using an URL.
                modifier = Modifier
                    .size(32.dp, 32.dp)
                    .clip(CircleShape)
                    .clickable { },
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
                Row {
                    Text(
                        text = comment.user?.login ?: "N/A",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Text(
                        text = ParseDateFormat.getTimeAgo(comment.created_at).toString(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (comment.author_association.lowercase(Locale.ROOT) != "none") {
                    Text(
                        text = comment.author_association.lowercase(Locale.ROOT),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_emoji),
                    contentDescription = "emoji",
                    tint = MaterialTheme.colorScheme.onSurface
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
                        contentDescription = "more option",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(text = {
                        Text(
                            text = "Edit",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                        onClick = {
                            onNavigate(
                                R.id.action_commitFragment_to_editCommentFragment,
                                comment.body,
                                comment.id
                            )
                            showMenu = false
                        })
                    DropdownMenuItem(text = {
                        Text(
                            text = "Delete",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, onClick = {
                        onCurrentSheetChanged(
                            CommitScreenSheets.CommitDeleteRequestSheet(
                                comment.id
                            )
                        )
                        onAction("delete", "")
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "Reply",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, onClick = {
                        onReply(comment.user?.login ?: "N/A")
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "Share",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, onClick = {
                        onAction("share", comment.html_url)
                        showMenu = false
                    })
                }
            }
        }

        Text(
            text = comment.body,
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, end = 8.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

