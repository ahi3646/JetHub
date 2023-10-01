package com.hasan.jetfasthub.screens.main.repository

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.main.home.presentation.IssuesItem
import com.hasan.jetfasthub.screens.main.repository.models.commits_model.CommitsModelItem
import com.hasan.jetfasthub.screens.main.repository.models.file_models.FileModel
import com.hasan.jetfasthub.screens.main.repository.models.release_download_model.ReleaseDownloadModel
import com.hasan.jetfasthub.screens.main.repository.models.releases_model.ReleasesModel
import com.hasan.jetfasthub.screens.main.repository.models.releases_model.ReleasesModelItem
import com.hasan.jetfasthub.screens.main.repository.models.repo_contributor_model.Contributors
import com.hasan.jetfasthub.screens.main.repository.models.repo_contributor_model.ContributorsItem
import com.hasan.jetfasthub.screens.main.repository.models.repo_model.RepoModel
import com.hasan.jetfasthub.screens.main.repository.models.tags_model.TagsModel
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesItem
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel
import com.hasan.jetfasthub.core.ui.utils.FileSizeCalculator
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.ParseDateFormat
import com.hasan.jetfasthub.core.ui.utils.RepoQueryProvider
import com.hasan.jetfasthub.core.ui.utils.Resource
import com.mukesh.MarkDown
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URL

class RepositoryFragment : Fragment() {

    private val repositoryViewModel: RepositoryViewModel by viewModel()
    private lateinit var token: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        token = PreferenceHelper(context).getToken()
        val owner = arguments?.getString("repository_owner")
        val repo = arguments?.getString("repository_name")

        if (owner != null && repo != null) {

            repositoryViewModel.setFields(owner, repo)

            repositoryViewModel.getRepo(
                token = token, owner = owner, repo = repo
            )

            repositoryViewModel.isWatchingRepo(
                token = token, owner = owner, repo = repo
            )

            repositoryViewModel.isStarringRepo(
                token = token, owner = owner, repo = repo
            )

            repositoryViewModel.getBranches(
                token = token, owner = owner, repo = repo
            )
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { branches ->

                    val branchesList = arrayListOf<String>()
                    branches.forEach {
                        branchesList.add(it.name)
                    }

                    val initialBranch = if (branchesList.isNotEmpty()) {
                        if (branchesList.contains("main")) {
                            "main"
                        } else if (branchesList.contains("master")) {
                            "master"
                        } else {
                            branchesList[0]
                        }
                    } else {
                        "main"
                    }

                    repositoryViewModel.updateFilesRef(initialBranch)
                    repositoryViewModel.updateCommitsRef(initialBranch)
                    repositoryViewModel.updateInitialBranch(initialBranch)

                    repositoryViewModel.getReadMeMarkdown(token, repo, owner, initialBranch)

                    repositoryViewModel.getBranch(
                        token = token,
                        owner = owner,
                        repo = repo,
                        branch = initialBranch
                    )

                    repositoryViewModel.getContentFiles(
                        token = token,
                        owner = owner,
                        repo = repo,
                        path = "",
                        ref = initialBranch
                    )

                    repositoryViewModel.getCommits(
                        token = token,
                        owner = owner,
                        repo = repo,
                        branch = initialBranch,
                        page = 1,
                        path = ""
                    )

                }
                .launchIn(lifecycleScope)

            repositoryViewModel.getTags(
                token = token, owner = owner, repo = repo, page = 1
            )

            repositoryViewModel.getLabels(
                token = token, owner = owner, repo = repo, page = 1
            )

            repositoryViewModel.getReleases(
                token = token, owner = owner, repo = repo, page = 1
            )

            repositoryViewModel.getContributors(
                token = token, owner = owner, repo = repo, page = 1
            )

            val issuesOpenQuery = RepoQueryProvider.getIssuesPullRequestQuery(
                owner,
                repo,
                IssueState.Open,
                false
            )
            repositoryViewModel.getIssuesWithCount(
                token = token,
                query = issuesOpenQuery,
                page = 1,
                IssueState.Open
            )

            val issuesCloseQuery = RepoQueryProvider.getIssuesPullRequestQuery(
                owner,
                repo,
                IssueState.Closed,
                false
            )
            repositoryViewModel.getIssuesWithCount(
                token = token,
                query = issuesCloseQuery,
                page = 1,
                IssueState.Closed
            )

            val pullsOpenQuery = RepoQueryProvider.getIssuesPullRequestQuery(
                owner,
                repo,
                IssueState.Open,
                true
            )
            repositoryViewModel.getPullWithCount(
                token = token,
                query = pullsOpenQuery,
                page = 1,
                IssueState.Open
            )

            val pullsCloseQuery = RepoQueryProvider.getIssuesPullRequestQuery(
                owner,
                repo,
                IssueState.Closed,
                true
            )
            repositoryViewModel.getPullWithCount(
                token = token,
                query = pullsCloseQuery,
                page = 1,
                IssueState.Closed
            )

        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val state by repositoryViewModel.state.collectAsState()
                JetFastHubTheme {
                    MainContent(
                        state = state,
                        onBottomBarClicked = { repositoryScreen ->
                            repositoryViewModel.onBottomBarItemClicked(repositoryScreen)
                        },
                        onCurrentSheetChanged = { currentSheet ->
                            repositoryViewModel.onBottomSheetChanged(currentSheet)
                        },
                        onNavigate = { dest, data, extra ->
                            when (dest) {
                                -1 -> {
                                    findNavController().popBackStack()
                                }

                                R.id.action_repositoryFragment_to_fileViewFragment -> {
                                    val bundle = Bundle()
                                    bundle.putString("file_path", data)
                                    bundle.putString("file_ref", state.FilesRef)
                                    bundle.putString("repo_owner", state.RepoOwner)
                                    bundle.putString("repo_name", state.RepoName)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_repositoryFragment_to_searchFragment -> {
                                    val bundle = bundleOf("repo_topic" to data!!)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_repositoryFragment_to_searchFilesFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_repositoryFragment_to_profileFragment -> {
                                    val bundle = Bundle()
                                    if (data != null) {
                                        bundle.putString("username", data)
                                    }
                                    if (extra != null) {
                                        bundle.putString("start_index", extra)
                                    }
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_repositoryFragment_self -> {
                                    val bundle = Bundle()
                                    if (data != null) {
                                        bundle.putString("repository_owner", data)
                                    }
                                    if (extra != null) {
                                        bundle.putString("repository_name", extra)
                                    }
                                    findNavController().navigate(dest, bundle)

                                }

                                R.id.action_repositoryFragment_to_commitFragment -> {
                                    val bundle = Bundle()
                                    bundle.putString("owner", state.RepoOwner)
                                    bundle.putString("repo", state.RepoName)
                                    if (data != null) {
                                        bundle.putString("sha", data)
                                    }
                                    findNavController().navigate(dest, bundle)
                                }
                            }
                        },
                        onDownload = {
                            repositoryViewModel.downloadRelease(it)
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

                                "download" -> {
                                    val message = Uri.parse(
                                        data!!
                                    ).lastPathSegment
                                    repositoryViewModel.downloadRepo(
                                        data,
                                        message ?: "jethub_download"
                                    )
                                }

                                "download_file" -> {
                                    val message = Uri.parse(
                                        data!!
                                    ).lastPathSegment
                                    repositoryViewModel.downloadFile(
                                        data,
                                        message ?: "jethub_download"
                                    )
                                }

                                "on_path_change" -> {
                                    val paths = state.Paths

                                    if (paths.contains(data) && data != paths[paths.lastIndex]) {
                                        val newPaths = paths.subList(
                                            0,
                                            state.Paths.indexOf(data) + 1
                                        )
                                        repositoryViewModel.updatePaths(newPaths)

                                        repositoryViewModel.getContentFiles(
                                            token = token,
                                            owner = state.RepoOwner,
                                            repo = state.RepoName,
                                            path = data!!,
                                            ref = state.FilesRef
                                        )

                                    } else if (paths.contains(data) && data == paths[paths.lastIndex]) {
                                        //just repeatable action
                                    } else {
                                        Log.d("ahi3646", "onCreateView: content - $data ")
                                        val newPaths = paths.toMutableList()
                                        newPaths.add(data ?: "")
                                        repositoryViewModel.updatePaths(newPaths)

                                        repositoryViewModel.getContentFiles(
                                            token = token,
                                            owner = state.RepoOwner,
                                            repo = state.RepoName,
                                            path = data!!,
                                            ref = state.FilesRef
                                        )
                                    }

                                }

                                "watch_repo" -> {
                                    repositoryViewModel.watchRepo(
                                        token, state.RepoOwner, state.RepoName
                                    ).flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
                                        if (it.subscribed) {
                                            repositoryViewModel.getRepo(
                                                token = token,
                                                owner = state.RepoOwner,
                                                repo = state.RepoName
                                            )
                                        }
                                    }.launchIn(lifecycleScope)
                                }

                                "unwatch_repo" -> {
                                    repositoryViewModel.unwatchRepo(
                                        token, state.RepoOwner, state.RepoName
                                    ).flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
                                        if (it) {
                                            repositoryViewModel.getRepo(
                                                token = token,
                                                owner = state.RepoOwner,
                                                repo = state.RepoName
                                            )
                                        }
                                    }.launchIn(lifecycleScope)
                                }

                                "star_repo" -> {
                                    repositoryViewModel.starRepo(
                                        token, state.RepoOwner, state.RepoName
                                    ).flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
                                        if (it) {
                                            Toast.makeText(
                                                requireContext(),
                                                "You starred repo",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            repositoryViewModel.getRepo(
                                                token = token,
                                                owner = state.RepoOwner,
                                                repo = state.RepoName
                                            )
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Action can't be done currently",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }.launchIn(lifecycleScope)
                                }

                                "un_star_repo" -> {
                                    repositoryViewModel.unStarRepo(
                                        token, state.RepoOwner, state.RepoName
                                    ).flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
                                        if (it) {
                                            Toast.makeText(
                                                requireContext(),
                                                "You unstarred repo",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            repositoryViewModel.getRepo(
                                                token = token,
                                                owner = state.RepoOwner,
                                                repo = state.RepoName
                                            )
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Action can't be done currently",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }.launchIn(lifecycleScope)
                                }

                                "fork_repo" -> {
                                    repositoryViewModel.forkRepo(
                                        token,
                                        state.RepoOwner,
                                        state.RepoName
                                    )
                                        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                                        .onEach {
                                            if (it) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "You forked this repository!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                repositoryViewModel.getRepo(
                                                    token = token,
                                                    owner = state.RepoOwner,
                                                    repo = state.RepoName
                                                )
                                            }
                                        }
                                        .launchIn(lifecycleScope)
                                }

                                "repo_download_link_change" -> {
                                    repositoryViewModel.updateDownloadLink(Resource.Success(data!!))
                                }

                                "files_branch_change" -> {

                                    repositoryViewModel.updateFilesRef(data ?: "main")

                                    repositoryViewModel.getBranch(
                                        token = token,
                                        owner = state.RepoOwner,
                                        repo = state.RepoName,
                                        branch = data ?: "main"
                                    )
                                    val newPaths = state.Paths.subList(0, 1)
                                    repositoryViewModel.updatePaths(newPaths)
                                    repositoryViewModel.getContentFiles(
                                        token = token,
                                        owner = state.RepoOwner,
                                        repo = state.RepoName,
                                        path = "",
                                        ref = data ?: "main"
                                    )
                                }

                                "files_tag_change" -> {
                                    repositoryViewModel.updateFilesRef(data!!)
                                    val newPaths = state.Paths.subList(0, 1)
                                    repositoryViewModel.updatePaths(newPaths)
                                    repositoryViewModel.getContentFiles(
                                        token = token,
                                        owner = state.RepoOwner,
                                        repo = state.RepoName,
                                        path = "",
                                        ref = data
                                    )
                                }

                                "commit_branch_change" -> {
                                    repositoryViewModel.updateCommitsRef(data ?: "main")
                                    repositoryViewModel.getCommits(
                                        token = token,
                                        owner = state.RepoOwner,
                                        repo = state.RepoName,
                                        branch = data ?: "main",
                                        page = 1,
                                        path = ""
                                    )
                                }

                                "commit_tag_change" -> {
                                    repositoryViewModel.updateCommitsRef(data ?: "main")
                                    repositoryViewModel.getCommits(
                                        token = token,
                                        owner = state.RepoOwner,
                                        repo = state.RepoName,
                                        branch = data ?: "main",
                                        page = 1,
                                        path = ""
                                    )
                                }
                            }
                        },
                        onIssueItemClicked = { dest, owner, repo, issueNumber ->
                            Log.d("ahi3646", "onCreateView: $owner  $repo  $issueNumber ")
                            val bundle = Bundle()
                            bundle.putString("issue_owner", owner)
                            bundle.putString("issue_repo", repo)
                            bundle.putString("issue_number", issueNumber)
                            findNavController().navigate(dest, bundle)
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    onDownload: (release: ReleaseDownloadModel) -> Unit,
    state: RepositoryScreenState,
    onBottomBarClicked: (RepositoryScreens) -> Unit,
    onNavigate: (Int, String?, String?) -> Unit,
    onAction: (String, String?) -> Unit,
    onCurrentSheetChanged: (BottomSheetScreens) -> Unit,
    onIssueItemClicked: (Int, String, String, String) -> Unit
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

    val sheetStateX = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
            when (state.currentSheet) {

                is BottomSheetScreens.ReleaseItemSheet -> {
                    ReleaseInfoSheet(
                        releaseItem = state.currentSheet.releaseItem, closeSheet = closeSheet
                    )
                }

                BottomSheetScreens.RepositoryInfoSheet -> {
                    RepositoryInfoSheet(
                        closeSheet = closeSheet,
                        state = state,
                    )
                }

                is BottomSheetScreens.RepoDownloadSheet -> {

                    RepoDownloadSheet(
                        url = state.currentSheet.url,
                        closeSheet = closeSheet,
                        onAction = onAction
                    )
                }

                BottomSheetScreens.ForkSheet -> {
                    Column(
                        Modifier
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Fork", color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("Fork")
                                append(" ")
                                append(state.Repository.data?.full_name)
                            },
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                closeSheet()
                                if (state.HasForked) {
                                    onAction("fork_repo", null)
                                }
                            }) {
                                Text(text = "YES")
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(onClick = {
                                closeSheet()
                            }) {
                                Text(text = "NO", color = Color.Red)
                            }
                        }
                    }
                }

            }
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContentColor = MaterialTheme.colorScheme.inverseOnSurface,
        sheetBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface
    ) { sheetPadding ->
        Scaffold(
            modifier = Modifier
                .padding(sheetPadding)
                .navigationBarsPadding(),
            topBar = {
                Column(Modifier.fillMaxWidth()) {
                    TitleHeader(
                        state = state.Repository,
                        onNavigate = onNavigate,
                        onCurrentSheetChanged = {
                            onCurrentSheetChanged(BottomSheetScreens.RepositoryInfoSheet)
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
                        state = state,
                        onNavigate = onNavigate,
                        onAction = onAction,
                        onCurrentSheetChanged = {
                            onCurrentSheetChanged(BottomSheetScreens.ForkSheet)
                            scope.launch {
                                if (sheetScaffoldState.bottomSheetState.isCollapsed) {
                                    sheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    sheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        },
                        onLicenseClicked = {
                            showBottomSheet = true
                        }
                    )
                }
            },
            bottomBar = {
                BottomNav(onBottomBarClicked, state.Repository)
            }
        ) { paddingValues ->

            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetStateX
                ) {
                    when (state.License) {
                        is Resource.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Loading ...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        is Resource.Success -> {
                            val content = state.License.data!!.body
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.8F)),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                MarkDown(
                                    text = content,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                                0.5F
                                            )
                                        ),
                                )
                            }
                        }

                        is Resource.Failure -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Can't get license",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            when (state.selectedBottomBarItem) {
                RepositoryScreens.Code -> CodeScreen(
                    onDownload = onDownload,
                    paddingValues = paddingValues,
                    state = state,
                    onNavigate = onNavigate,
                    onCurrentSheetChanged = { bottomSheet ->
                        onCurrentSheetChanged(bottomSheet)
                        scope.launch {
                            if (sheetScaffoldState.bottomSheetState.isCollapsed) {
                                sheetScaffoldState.bottomSheetState.expand()
                            } else {
                                sheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    },
                    onAction = onAction
                )

                RepositoryScreens.Issues -> IssuesScreen(
                    paddingValues = paddingValues,
                    state = state,
                    onIssueItemClicked = onIssueItemClicked
                )

                RepositoryScreens.PullRequest -> PullRequestsScreen(
                    paddingValues = paddingValues,
                    state = state,
                )
                RepositoryScreens.Projects -> {
                    if ((state.Repository.data != null) && state.Repository.data.has_projects) {
                        ProjectsScreen(paddingValues = paddingValues)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReleaseInfoSheet(releaseItem: ReleasesModelItem, closeSheet: () -> Unit) {
    Column(
        Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = releaseItem.name, style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (releaseItem.body != null) {
            Text(
                text = releaseItem.body.toString(),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

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

@Composable
private fun RepoDownloadSheet(
    url: Resource<String>,
    closeSheet: () -> Unit,
    onAction: (String, String?) -> Unit
) {
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Download",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Are you sure ?", color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                closeSheet()
            }) {
                Text(text = "No", color = Color.Red)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = {
                onAction("download", url.data)
                closeSheet()
            }) {
                Text(text = "Yes", color = Color.Blue)
            }
        }
    }
}

@Composable
private fun RepositoryInfoSheet(state: RepositoryScreenState, closeSheet: () -> Unit) {
    val repository = state.Repository
    if (repository.data != null) {
        Column(
            Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = repository.data.full_name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (repository.data.description != null) {
                Text(
                    text = repository.data.description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    closeSheet()
                }) {
                    Text(text = "OK")
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Composable
private fun CodeScreen(
    onDownload: (release: ReleaseDownloadModel) -> Unit,
    paddingValues: PaddingValues,
    state: RepositoryScreenState,
    onNavigate: (Int, String?, String?) -> Unit,
    onCurrentSheetChanged: (bottomSheet: BottomSheetScreens) -> Unit,
    onAction: (String, String?) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("README", "FILES", "COMMITS", "RELEASE", "CONTRIBUTORS")

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        if (tabIndex == index) {
                            Text(
                                title,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(
                                title,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
        when (tabIndex) {
            0 -> ReadMeScreen(state)
            1 -> FilesScreen(
                state = state,
                onAction = onAction,
                onCurrentSheetChanged = onCurrentSheetChanged,
                onNavigate = onNavigate
            )
            2 -> CommitsScreen(state, onAction, onNavigate)
            3 -> ReleasesScreen(
                onDownload = onDownload,
                releases = state.Releases,
                onCurrentSheetChanged = onCurrentSheetChanged
            )

            4 -> ContributorsScreen(state.Contributors, onNavigate)
        }
    }
}

@Composable
private fun SwitchBranchDialog(
    branchActionName: String,
    tagActionName: String,
    closeDialog: () -> Unit,
    onAction: (String, String?) -> Unit,
    branches: List<String>,
    tags: TagsModel,
    onDialogShown: () -> Unit
) {

    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("BRANCHES", "TAGS")

    Column(
        modifier = Modifier
            .fillMaxWidth(0.95F)
            .fillMaxHeight(0.95F)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Surface {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    IconButton(onClick = { onDialogShown() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "Switch branch",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                    Text(
                        text = "Releases",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                TabRow(
                    selectedTabIndex = tabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tabIndex == index, onClick = { tabIndex = index },
                            text = {
                                if (tabIndex == index) {
                                    Text(
                                        title,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    Text(
                                        title,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            },
                            selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
            }
        }

        when (tabIndex) {

            0 -> {
                Spacer(modifier = Modifier.height(6.dp))
                LazyColumn {
                    itemsIndexed(branches) { index, branch ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAction(branchActionName, branch)
                                    closeDialog()
                                },
                            elevation = 0.dp,
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fork),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = branch,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 12.dp,
                                        bottom = 12.dp
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        if (index < branches.lastIndex) {
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier
                                    .height(0.5.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }

            1 -> {
                if (tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyColumn {
                        itemsIndexed(tags) { index, tag ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (tagActionName == "files_tag_change") {
                                            onAction("repo_download_link_change", tag.zipball_url)
                                        }
                                        onAction(tagActionName, tag.name)
                                        closeDialog()
                                    },
                                elevation = 0.dp,
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.padding(start = 16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_label),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = tag.name,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            if (index < tags.lastIndex) {
                                Divider(
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .height(0.5.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Tags not found !",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilesScreen(
    state: RepositoryScreenState,
    onAction: (String, String?) -> Unit,
    onCurrentSheetChanged: (bottomSheet: BottomSheetScreens) -> Unit,
    onNavigate: (Int, String?, String?) -> Unit
) {

    when (state.RepositoryFiles) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val files = state.RepositoryFiles
            val branches = state.Branches.data!!
            val tags = state.Tags.data!!

            val branchList = arrayListOf<String>()
            branches.forEach {
                branchList.add(it.name)
            }

            var isDialogShown by remember {
                mutableStateOf(false)
            }

            if (isDialogShown) {
                Dialog(
                    onDismissRequest = { isDialogShown = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = false
                    ),
                    content = {
                        SwitchBranchDialog(
                            branchActionName = "files_branch_change",
                            tagActionName = "files_tag_change",
                            closeDialog = { isDialogShown = false },
                            onAction = onAction,
                            branches = branchList,
                            tags = tags,
                            onDialogShown = { isDialogShown = false },
                        )
                    }
                )
            }

            Column(
                Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 18.dp, end = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_branch),
                        contentDescription = "branch icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)
                            .clickable {
                                isDialogShown = true
                            }
                    ) {
                        Text(
                            text = state.FilesRef,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 8.dp, top = 10.dp, bottom = 10.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.weight(1F))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dropdown_icon),
                            contentDescription = "dropdown",
                            modifier = Modifier.padding(start = 4.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    IconButton(onClick = {
                        onAction("on_path_change", "")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "direction",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    LazyRow(Modifier.weight(1F)) {
                        items(state.Paths) { path ->
                            FilePathRowItemCard(path, onAction)
                        }
                    }

                    when (state.RepoDownloadLink) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                strokeWidth = 4.dp
                            )
                        }

                        is Resource.Success -> {
                            IconButton(
                                onClick = {
                                    onCurrentSheetChanged(
                                        BottomSheetScreens.RepoDownloadSheet(
                                            state.RepoDownloadLink
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_download),
                                    contentDescription = "download",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        is Resource.Failure -> {}
                    }

                    if (state.Repository.data!!.permissions.admin) {
                        IconButton(onClick = {
                            //implement action
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "add icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }

                    IconButton(onClick = {
                        onNavigate(
                            R.id.action_repositoryFragment_to_searchFilesFragment,
                            null,
                            null
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "direction",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    //sort directions and folders
                    files.data!!.sortBy { fileModel ->
                        fileModel.type
                    }

                    items(files.data) { file ->
                        when (file.type) {
                            "dir" -> {
                                FileFolderItemCard(
                                    file = file,
                                    onAction = onAction,
                                )
                            }

                            "file" -> {
                                FileDocumentItemCard(
                                    file = file,
                                    onAction = onAction,
                                    onNavigate = onNavigate,
                                )
                            }

                            else -> {}
                        }
                    }
                }

            }

        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

}


@Composable
private fun FilePathRowItemCard(path: String, onAction: (String, String?) -> Unit) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
            .clickable {
                onAction("on_path_change", path)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = path.substring(path.lastIndexOf("/") + 1),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = "path",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
private fun FileFolderItemCard(
    file: FileModel,
    onAction: (String, String?) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAction("on_path_change", file.path)
            },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_folder),
                contentDescription = "folder icon",
                tint = Color.Blue
            )

            Text(
                text = file.name,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1F),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Box {
                IconButton(
                    onClick = {
                        showMenu = !showMenu
                    },
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "more option",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(text = {
                        Text(
                            text = "Share",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("share", file.html_url)
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "Copy URL",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("copy", file.html_url)
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "File History",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("file_history", file.html_url)
                        showMenu = false
                    })
                }
            }
        }
    }

}

@Composable
private fun FileDocumentItemCard(
    file: FileModel,
    onAction: (String, String?) -> Unit,
    onNavigate: (Int, String, String?) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigate(R.id.action_repositoryFragment_to_fileViewFragment, file.path, null)
            },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_document),
                contentDescription = "Document icon",
                tint = Color.Blue
            )

            Text(
                text = file.name,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1F),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Box {
                IconButton(
                    onClick = {
                        showMenu = !showMenu
                    },
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "more option",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(text = {
                        Text(
                            text = "Download",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("download_file", file.download_url)
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "Share",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("share", file.html_url)
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "Copy URL",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("copy", file.html_url)
                        showMenu = false
                    })
                    DropdownMenuItem(text = {
                        Text(
                            text = "File History",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }, onClick = {
                        onAction("file_history", file.html_url)
                        showMenu = false
                    })
                }
            }
        }
    }
}

@Composable
private fun CommitsScreen(
    state: RepositoryScreenState,
    onAction: (String, String?) -> Unit,
    onItemClicked: (Int, String?, String?) -> Unit
) {
    when (state.Commits) {

        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val commits = state.Commits

            val branches = state.Branches.data!!
            val tags = state.Tags.data!!

            val branchList = arrayListOf<String>()
            branches.forEach {
                branchList.add(it.name)
            }

            var isDialogShown by remember {
                mutableStateOf(false)
            }

            if (isDialogShown) {
                Dialog(
                    onDismissRequest = { isDialogShown = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = false
                    ),
                    content = {
                        SwitchBranchDialog(
                            branchActionName = "commit_branch_change",
                            tagActionName = "commit_tag_change",
                            closeDialog = { isDialogShown = false },
                            onAction = onAction,
                            branches = branchList,
                            tags = tags,
                            onDialogShown = { isDialogShown = false },
                        )
                    }
                )
            }

            Column(
                Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 18.dp, end = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_branch),
                        contentDescription = "branch icon"
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)
                            .clickable {
                                isDialogShown = true
                            }
                    ) {
                        Text(
                            text = state.CommitsRef,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 8.dp, top = 10.dp, bottom = 10.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(Modifier.weight(1F))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dropdown_icon),
                            contentDescription = "dropdown",
                        )
                    }
                }

                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .height(0.5.dp)
                        .padding(start = 6.dp, end = 6.dp)
                        .fillMaxWidth()
                        .align(Alignment.End)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    itemsIndexed(commits.data!!) { index, commit ->
                        CommitsItem(commit, onItemClicked)
                        if (index < commits.data.lastIndex) {
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier
                                    .height(0.5.dp)
                                    .padding(start = 72.dp, end = 6.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.End)
                            )
                        }
                    }
                }

            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun CommitsItem(commit: CommitsModelItem, onItemClicked: (Int, String?, String?) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onItemClicked(
                        R.id.action_repositoryFragment_to_commitFragment,
                        commit.sha,
                        null
                    )
                })
            .padding(4.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            GlideImage(
                failure = {
                    // i dunno but this line didn't triggered when the state was failure
                    painterResource(id = R.drawable.baseline_account_circle_24)
                },
                imageModel = {
                    if (commit.author != null) {
                        commit.author.avatar_url
                    } else {
                        R.drawable.baseline_account_circle_24
                    }
                },
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .size(48.dp, 48.dp)
                    .clip(CircleShape),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterStart,
                    contentDescription = "Actor Avatar"
                )
            )

//                Image(
//                    painter = painterResource(id = R.drawable.baseline_account_circle_24),
//                    contentDescription = "avatar icon",
//                    modifier = Modifier
//                        .size(48.dp, 48.dp)
//                        .size(48.dp, 48.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop,
//                )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = commit.commit.message,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = buildAnnotatedString {
                            if (commit.author != null) {
                                append(commit.author.login)
                            } else {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("N/A")
                                }
                            }
                            append(" ")
                            append(ParseDateFormat.getTimeAgo(commit.commit.author.date).toString())
                        },
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(Modifier.weight(1F))

                    if (commit.commit.comment_count != 0) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_comment_small),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = commit.commit.comment_count.toString(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun ReleasesScreen(
    onDownload: (release: ReleaseDownloadModel) -> Unit,
    releases: Resource<ReleasesModel>,
    onCurrentSheetChanged: (bottomSheet: BottomSheetScreens) -> Unit
) {
    when (releases) {

        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            if (releases.data!!.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    itemsIndexed(releases.data) { index, release ->
                        ReleaseItemCard(
                            onDownload = onDownload,
                            releasesModelItem = release,
                            onCurrentSheetChanged = onCurrentSheetChanged
                        )
                        if (index < releases.data.lastIndex) {
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No releases !", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

    }
}

@Composable
private fun ReleaseItemCard(
    onDownload: (release: ReleaseDownloadModel) -> Unit,
    releasesModelItem: ReleasesModelItem,
    onCurrentSheetChanged: (bottomSheet: BottomSheetScreens) -> Unit,
) {

    var isDialogShown by remember {
        mutableStateOf(false)
    }

    val releases = arrayListOf<ReleaseDownloadModel>()

    if (releasesModelItem.zipball_url.isNotEmpty()) {
        releases.add(
            ReleaseDownloadModel(
                title = "Source code (zip)",
                url = releasesModelItem.tarball_url,
                extension = "application/zip",
                downloadCount = 0,
                notificationTitle = releasesModelItem.tag_name
            )
        )
    }
    if (releasesModelItem.tarball_url.isNotEmpty()) {
        releases.add(
            ReleaseDownloadModel(
                title = "Source code (tar.gz)",
                url = releasesModelItem.tarball_url,
                extension = "application/x-gzip",
                downloadCount = 0,
                notificationTitle = releasesModelItem.tag_name
            )
        )
    }
    if (releasesModelItem.assets.isNotEmpty()) {
        releasesModelItem.assets.forEach { asset ->
            releases.add(
                ReleaseDownloadModel(
                    title = asset.name,
                    url = asset.browser_download_url,
                    extension = asset.content_type,
                    downloadCount = asset.download_count,
                    notificationTitle = releasesModelItem.tag_name
                )
            )
        }
    }

    if (isDialogShown) {
        Dialog(
            onDismissRequest = { isDialogShown = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .fillMaxHeight(0.33f)
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "Releases",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyColumn {
                        itemsIndexed(releases) { index, release ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isDialogShown = false
                                        onDownload(release)
                                    },
                                elevation = 0.dp,
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                val title = if (release.downloadCount != 0) {
                                    "${release.title} (${release.downloadCount})"
                                } else release.title
                                Text(
                                    text = title,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(
                                        start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            if (index < releases.lastIndex) {
                                Divider(
                                    color = Color.Gray, modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onCurrentSheetChanged(BottomSheetScreens.ReleaseItemSheet(releasesModelItem))
            })
            .padding(4.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = releasesModelItem.tag_name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = buildAnnotatedString {
                        append(releasesModelItem.author.login)
                        append(" ")
                        if (releasesModelItem.draft) {
                            append("Drafted")
                        } else {
                            append("Released")
                        }
                        append(" ")
                        append(
                            ParseDateFormat.getTimeAgo(releasesModelItem.created_at).toString()
                        )
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    isDialogShown = true
                },
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .size(48.dp, 48.dp)
                    .clip(CircleShape),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = "release download",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

    }
}

@Composable
private fun ReadMeScreen(state: RepositoryScreenState) {
    if (state.InitialBranch == "") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else if(state.HasMarkdown){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.8F)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            MarkDown(
                url = URL("https://raw.githubusercontent.com/${state.RepoOwner}/${state.RepoName}/${state.InitialBranch}/README.md"),
                modifier = Modifier.fillMaxSize()
            )
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No data available", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ContributorsScreen(
    contributors: Resource<Contributors>,
    onItemClicked: (Int, String?, String?) -> Unit,
) {
    when (contributors) {

        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(contributors.data!!) { index, contributor ->
                    ContributorsItemCard(
                        contributor, onItemClicked
                    )
                    if (index < contributors.data.lastIndex) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(start = 74.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ContributorsItemCard(
    contributorsItem: ContributorsItem, onItemClicked: (Int, String?, String?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onItemClicked(
                        R.id.action_repositoryFragment_to_profileFragment,
                        contributorsItem.login,
                        null
                    )
                })
            .padding(4.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            GlideImage(
                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                imageModel = {
                    contributorsItem.avatar_url
                }, // loading a network image using an URL.
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .size(48.dp, 48.dp)
                    .clip(CircleShape),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterStart,
                    contentDescription = "Actor Avatar"
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = contributorsItem.login,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Commits (${contributorsItem.contributions})",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
        }
    }
}

@Composable
private fun IssuesScreen(
    paddingValues: PaddingValues,
    state: RepositoryScreenState,
    onIssueItemClicked: (Int, String, String, String) -> Unit
) {
    val tabs = listOf("OPENED", "CLOSED")
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier
                .fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index, onClick = { tabIndex = index },
                    text = {
                        if (tabIndex == index) {
                            androidx.compose.material3.Text(
                                title,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            androidx.compose.material3.Text(
                                title,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
        when (tabIndex) {
            0 -> IssuesScreenContent(
                state = state.OpenIssues,
                onIssueItemClicked = onIssueItemClicked,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            1 -> IssuesScreenContent(
                state = state.ClosedIssues,
                onIssueItemClicked = onIssueItemClicked,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}

@Composable
private fun IssuesScreenContent(
    state: Resource<IssuesModel>,
    onIssueItemClicked: (Int, String, String, String) -> Unit,
    modifier: Modifier
) {
    when (state) {
        is Resource.Loading -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val issues = state.data!!.items
            if (issues.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(issues) { issue ->
                        IssuesItem(issue = issue, onIssueItemClicked = onIssueItemClicked)
                    }
                }
            } else {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No issues", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PullRequestsScreen(
    paddingValues: PaddingValues,
    state: RepositoryScreenState
) {
    val tabs = listOf("OPENED", "CLOSED")
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier
                .fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index, onClick = { tabIndex = index },
                    text = {
                        if (tabIndex == index) {
                            androidx.compose.material3.Text(
                                title,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            androidx.compose.material3.Text(
                                title,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    },
                )
            }
        }
        when (tabIndex) {
            0 -> PullRequestScreenContent(data = state.OpenPulls)
            1 -> PullRequestScreenContent(data = state.ClosedPulls)
        }
    }
}

@Composable
private fun PullRequestScreenContent(
    data: Resource<IssuesModel>,
) {
    when (data) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading ...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        is Resource.Success -> {
            val pulls = data.data!!.items
            if (pulls.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(pulls) { pull ->
                        PullsItem(pull)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No pulls", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is Resource.Failure -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Can't load data!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PullsItem(
    pull: IssuesItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {

                }
            )
            .padding(8.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Text(
                text = pull.title,
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            val repoUrl = Uri.parse(pull.repository_url).pathSegments
            val repoName = repoUrl[repoUrl.lastIndex - 1] + "/" + repoUrl[repoUrl.lastIndex]

            Row {
                Text(
                    text = buildAnnotatedString {
                        append(repoName)
                        append("#${pull.number}")
                        append(" ")
                        if (pull.state == "closed") {
                            append(pull.state)
                            append(ParseDateFormat.getTimeAgo(pull.closed_at.toString()).toString())
                        } else {
                            append("${pull.state}ed")
                            append(ParseDateFormat.getTimeAgo(pull.created_at).toString())
                        }
                    },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1F),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ProjectsScreen(paddingValues: PaddingValues) {
    val tabs = listOf("OPENED", "CLOSED")
    var tabIndex by remember { mutableIntStateOf(0) }

    TabRow(
        selectedTabIndex = tabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index, onClick = { tabIndex = index },
                text = {
                    if (tabIndex == index) {
                        androidx.compose.material3.Text(
                            title,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        androidx.compose.material3.Text(
                            title,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
    when (tabIndex) {
        0 -> {}
        1 -> {}
    }
}

@Composable
private fun BottomNav(
    onBottomBarClicked: (RepositoryScreens) -> Unit, state: Resource<RepoModel>
) {
    val context = LocalContext.current

    when (state) {

        is Resource.Loading -> {
            Surface(elevation = 16.dp) {
                BottomAppBar(contentColor = MaterialTheme.colorScheme.surface) {
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_code_24),
                                contentDescription = "Code Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = {
                            Text(
                                "Code",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = {},
                    )

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                                contentDescription = "Issues Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text("Issues", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = {},
                    )

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                                contentDescription = "PullRequest Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = {
                            Text(
                                "Pull Requests",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                softWrap = false,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = {},
                    )
                }
            }
        }

        is Resource.Success -> {
            val repository = state.data!!

            var isCodeCurrent by remember {
                mutableStateOf(true)
            }
            var isIssuesCurrent by remember {
                mutableStateOf(false)
            }
            var isPRCurrent by remember {
                mutableStateOf(false)
            }
            var isProjectsCurrent by remember {
                mutableStateOf(false)
            }

            Surface(elevation = 16.dp) {
                BottomAppBar(contentColor = MaterialTheme.colorScheme.surface) {
                    BottomNavigationItem(
                        alwaysShowLabel = isCodeCurrent,
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_code_24),
                                contentDescription = "Code Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = {
                            Text(
                                "Code",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = {
                            isCodeCurrent = true
                            isIssuesCurrent = false
                            isPRCurrent = false
                            isProjectsCurrent = false
                            onBottomBarClicked(RepositoryScreens.Code)
                        },
                    )

                    BottomNavigationItem(
                        alwaysShowLabel = isIssuesCurrent,
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                                contentDescription = "Issues Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text("Issues", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = {
                            if (repository.has_issues) {
                                isCodeCurrent = false
                                isIssuesCurrent = true
                                isPRCurrent = false
                                isProjectsCurrent = false

                                onBottomBarClicked(RepositoryScreens.Issues)
                            } else {
                                Toast.makeText(
                                    context, "Issue section has been disabled", Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                    )

                    BottomNavigationItem(
                        alwaysShowLabel = isPRCurrent,
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                                contentDescription = "PullRequest Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = {
                            Text(
                                "Pull Requests",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                softWrap = false,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = {
                            isCodeCurrent = false
                            isIssuesCurrent = false
                            isPRCurrent = true
                            isProjectsCurrent = false
                            onBottomBarClicked(RepositoryScreens.PullRequest)
                        },
                    )

                    BottomNavigationItem(
                        alwaysShowLabel = isProjectsCurrent,
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_project),
                                contentDescription = "PullRequest Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text("Projects", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = {
                            isCodeCurrent = false
                            isIssuesCurrent = false
                            isPRCurrent = false
                            isProjectsCurrent = true
                            onBottomBarClicked(RepositoryScreens.Projects)
                        },
                    )
                }
            }
        }

        is Resource.Failure -> {
            Surface(elevation = 16.dp) {
                BottomAppBar(contentColor = MaterialTheme.colorScheme.surface) {
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_code_24),
                                contentDescription = "Code Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = {
                            Text(
                                "Code",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = {},
                    )

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_issues),
                                contentDescription = "Issues Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text("Issues", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = {},
                    )

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_pull_requests),
                                contentDescription = "PullRequest Screen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = {
                            Text(
                                "Pull Requests",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                softWrap = false,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleHeader(
    state: Resource<RepoModel>,
    onNavigate: (Int, String?, String?) -> Unit,
    onCurrentSheetChanged: () -> Unit
) {
    when (state) {

        is Resource.Loading -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "avatar icon",
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .size(48.dp, 48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }

        is Resource.Success -> {
            val repository = state.data!!

            var hasTopics by remember {
                mutableStateOf(false)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
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
                                onNavigate(
                                    R.id.action_repositoryFragment_to_profileFragment,
                                    repository.owner.login,
                                    null
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
                        Modifier.weight(1F),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = repository.full_name,
                            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row {
                            Text(
                                text = ParseDateFormat.getTimeAgo(repository.pushed_at).toString(),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = FileSizeCalculator.humanReadableByteCountBin(repository.size.toLong()),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
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

                    if (repository.topics.isNotEmpty()) {
                        IconButton(onClick = {
                            hasTopics = !hasTopics
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_label),
                                contentDescription = "label",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(onClick = {
                        onCurrentSheetChanged()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info_outline),
                            contentDescription = "info",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (hasTopics) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(repository.topics) { topic ->
                            Surface(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clickable {
                                        onNavigate(
                                            R.id.action_repositoryFragment_to_searchFragment,
                                            topic,
                                            null
                                        )
                                    },
                                contentColor = colorResource(id = R.color.milt_black),
                                color = Color.Gray
                            ) {
                                Text(
                                    text = topic, Modifier.padding(
                                        top = 4.dp, bottom = 4.dp, start = 6.dp, end = 6.dp
                                    ), color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }

        is Resource.Failure -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "avatar icon",
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .size(48.dp, 48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }

    }
}

@Composable
private fun Toolbar(
    state: RepositoryScreenState,
    onNavigate: (Int, String?, String?) -> Unit,
    onAction: (String, String?) -> Unit,
    onCurrentSheetChanged: () -> Unit,
    onLicenseClicked: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    when (state.Repository) {

        is Resource.Loading -> {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = { onNavigate(-1, null, null) }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
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
                                contentDescription = "Watch",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {

                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fork),
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_pin),
                                contentDescription = "Pin",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Box {
                    IconButton(
                        onClick = {
                            showMenu = !showMenu
                        },
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
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
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(
                                text = "Open in browser",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(
                                text = "Copy URL",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            showMenu = false
                        })
                    }
                }
            }
        }

        is Resource.Success -> {
            val repository = state.Repository.data!!

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = { onNavigate(-1, null, null) }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
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
                        IconButton(onClick = {
                            if (state.isWatching) {
                                onAction("unwatch_repo", null)
                            } else {
                                onAction("watch_repo", null)
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_eye),
                                contentDescription = "Watch",
                                tint = if (state.isWatching) Color.Blue else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = repository.subscribers_count.toString(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            if (state.isStarring) {
                                onAction("un_star_repo", null)
                            } else {
                                onAction("star_repo", null)
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_filled),
                                contentDescription = "Star",
                                tint = if (state.isStarring) Color.Blue else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = repository.stargazers_count.toString(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            onCurrentSheetChanged()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fork),
                                contentDescription = "Star",
                                tint = if (state.HasForked) Color.Blue else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = repository.forks_count.toString(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (repository.has_wiki) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_book),
                                    contentDescription = "Star",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(text = "Wiki", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_pin),
                                contentDescription = "Pin",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(text = "Pin", color = MaterialTheme.colorScheme.onSurface)
                    }

                    if (repository.license != null) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(onClick = { onLicenseClicked() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_license),
                                    contentDescription = "License",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = repository.license.spdx_id,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface

                            )
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
                    ) {
                        DropdownMenuItem(text = {
                            Text(
                                text = "Share",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            onAction("share", repository.html_url)
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(
                                text = "Open in browser",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            onAction("browser", repository.html_url)
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(
                                text = "Copy URL",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            onAction("copy", repository.html_url)
                            showMenu = false
                        })
                        if (repository.fork) {
                            DropdownMenuItem(text = { Text(text = repository.parent.full_name) },
                                onClick = {
                                    onNavigate(
                                        R.id.action_repositoryFragment_self,
                                        repository.parent.owner.login,
                                        repository.parent.name
                                    )
                                    showMenu = false
                                })
                        }
                    }
                }

            }
        }

        is Resource.Failure -> {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = { onNavigate(-1, null, null) }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
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
                                contentDescription = "Watch",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fork),
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_pin),
                                contentDescription = "Pin",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Box {
                    IconButton(
                        onClick = {
                            showMenu = !showMenu
                        },
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
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
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(
                                text = "Open in browser",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(
                                text = "Copy URL",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            showMenu = false
                        })
                    }
                }
            }
        }

    }
}


