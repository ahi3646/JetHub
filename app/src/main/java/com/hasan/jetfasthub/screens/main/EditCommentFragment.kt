package com.hasan.jetfasthub.screens.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.commits.EditCommentViewModel
import com.hasan.jetfasthub.screens.main.gists.GistViewModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditCommentFragment : Fragment() {

    private val editCommentViewModel: EditCommentViewModel by viewModel()
    private val gistViewModel: GistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val token = PreferenceHelper.getToken(requireContext())
        val owner = arguments?.getString("owner", "")
        val repo = arguments?.getString("repo", "")
        val editComment = arguments?.getString("edit_comment", "")
        val commentId = arguments?.getInt("comment_id", 0)

        return ComposeView(requireContext()).apply {
            setContent {
                JetFastHubTheme {
                    MainContent(
                        editComment = editComment!!,
                        onEdit = { body ->

                            gistViewModel.starGist(token, "7d14b6af5676917f7c188dcf7adb1bd6")

                            Log.d("ahi3646", "onCreateView: on Edit ")

                            editCommentViewModel.editComment(
                                token = token,
                                owner = owner!!,
                                repo = repo!!,
                                commentId = commentId!!,
                                body = body
                            )
                                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                                .onEach { response ->
                                    if (response) {
                                        findNavController().popBackStack()
                                        Toast.makeText(
                                            requireContext(),
                                            "Changed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Can't edit comment !",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().popBackStack()
                                    }
                                }

                            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()

                        },
                        onDiscard = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun MainContent(
    editComment: String,
    onEdit: (body: String) -> Unit,
    onDiscard: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val windowInfo = LocalWindowInfo.current
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = editComment,
                selection = TextRange(editComment.length)
            )
        )
    }

    LaunchedEffect(windowInfo) {
        snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
            if (isWindowFocused) {
                focusRequester.requestFocus()
            }
        }
    }

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
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Close",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Discard unsaved changes? ?")
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(onClick = { closeSheet() }) {
                        Text(text = "Cancel", color = Color.Red)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(onClick = {
                        closeSheet()
                        onDiscard()
                    }) {
                        Text(text = "Discard", color = Color.Blue)
                    }
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) { sheetPadding ->

        Scaffold(
            modifier = Modifier.padding(sheetPadding),
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 10.dp,
                    content = {
                        TopAppBarContent(
                            onBackPressed = {
                                keyboardController?.hide()
                                scope.launch {
                                    if (sheetScaffoldState.bottomSheetState.isCollapsed) {
                                        sheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        sheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            },
                            onSend = {
                                if (textFieldValueState.text.length >= 2) {
                                    if (textFieldValueState.text != editComment) {
                                        keyboardController?.hide()
                                        onEdit(textFieldValueState.text)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Do changes to edit !",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Min length for comment is 2 !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    },
                )
            },
        ) { paddingValues ->

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
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .padding(paddingValues),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                enabled = true
            )
        }
    }

}

@Composable
private fun TopAppBarContent(
    onBackPressed: () -> Unit,
    onSend: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            onBackPressed()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = "clear button"
            )
        }

        Text(
            text = "Markdown",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(1F)
        )

        IconButton(onClick = { onSend() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "send icon"
            )
        }
    }

}
