package com.hasan.jetfasthub.screens.main.home.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.core.ui.utils.RepoQueryProvider
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var token: String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //TODO fix preference helper later
        val token = PreferenceHelper(context).getToken()
        val authenticatedUser = PreferenceHelper(context).getAuthenticatedUsername()

        if (token != "" && authenticatedUser != "") {
            homeViewModel.getUser(token, authenticatedUser)
            homeViewModel.getEvents()

            val createdIssuesState = if (homeViewModel.state.value.issueScreenState[0]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getIssuesWithCount(
                token = token,
                query = getUrlForIssues(
                    MyIssuesType.CREATED,
                    createdIssuesState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.CREATED
            )


            val assignedIssuesState = if (homeViewModel.state.value.issueScreenState[1]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getIssuesWithCount(
                token = token,
                query = getUrlForIssues(
                    MyIssuesType.ASSIGNED,
                    assignedIssuesState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.ASSIGNED
            )


            val mentionedIssuesState = if (homeViewModel.state.value.issueScreenState[2]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getIssuesWithCount(
                token = token,
                query = getUrlForIssues(
                    MyIssuesType.MENTIONED,
                    mentionedIssuesState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.MENTIONED
            )


            val participatedIssuesState = if (homeViewModel.state.value.issueScreenState[3]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getIssuesWithCount(
                token = token,
                query = getUrlForIssues(
                    MyIssuesType.PARTICIPATED,
                    participatedIssuesState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.PARTICIPATED
            )


            val createdPullsState = if (homeViewModel.state.value.pullScreenState[0]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getPullsWithCount(
                token = token,
                query = getUrlForPulls(
                    MyIssuesType.CREATED,
                    createdPullsState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.CREATED
            )


            val assignedPullsState = if (homeViewModel.state.value.pullScreenState[1]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getPullsWithCount(
                token = token,
                query = getUrlForPulls(
                    MyIssuesType.ASSIGNED,
                    assignedPullsState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.ASSIGNED
            )


            val mentionedPullsState = if (homeViewModel.state.value.pullScreenState[2]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getPullsWithCount(
                token = token,
                query = getUrlForPulls(
                    MyIssuesType.MENTIONED,
                    mentionedPullsState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.MENTIONED
            )


            val reviewPullsState = if (homeViewModel.state.value.pullScreenState[3]) {
                IssueState.Open
            } else {
                IssueState.Closed
            }
            homeViewModel.getPullsWithCount(
                token = token,
                query = getUrlForPulls(
                    MyIssuesType.REVIEW,
                    reviewPullsState,
                    authenticatedUser
                ),
                page = 1,
                issuesType = MyIssuesType.REVIEW
            )

        } else {
            Toast.makeText(
                context,
                "Can't load user data. Please try to resign in!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val state by homeViewModel.state.collectAsState()

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scaffoldState = rememberScaffoldState(drawerState = drawerState)
                val drawerScope = rememberCoroutineScope()

                val isRefreshing by homeViewModel.isRefreshing.collectAsState()
                val pullRefreshState = rememberPullRefreshState(isRefreshing, { homeViewModel.refresh() })

                activity?.onBackPressedDispatcher?.addCallback(
                    owner = viewLifecycleOwner,
                    onBackPressedCallback = object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (scaffoldState.drawerState.isOpen) {
                                drawerScope.launch {
                                    drawerState.close()
                                }
                            } else {
                                isEnabled = false
                                activity?.onBackPressedDispatcher!!.onBackPressed()
                            }
                        }
                    }
                )

                JetFastHubTheme {
                    HomeScreen(
                        state = state,
                        isRefreshing = isRefreshing,
                        pullRefreshState = pullRefreshState,
                        onBottomBarItemSelected = homeViewModel::onBottomBarItemSelected,
                        onNavigate = { dest, data, extra ->
                            when (dest) {

                                -1 -> {
                                    findNavController().popBackStack()
                                }

                                R.id.action_homeFragment_to_notificationsFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_searchFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_repositoryFragment -> {
                                    val bundle = Bundle()
                                    bundle.putString("repository_owner", data)
                                    bundle.putString("repository_name", extra)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_homeFragment_to_profileFragment -> {
                                    Log.d("ahi3646", "onCreateView: $data -- $extra ")
                                    val bundle = Bundle()
                                    bundle.putString("username", data)
                                    bundle.putString("start_index", extra)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_homeFragment_to_pinnedFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_gistsFragment -> {
                                    val bundle = bundleOf("gist_data" to data)
                                    findNavController().navigate(dest, bundle)
                                }

                                R.id.action_homeFragment_to_faqFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_settingsFragment -> {
                                    findNavController().navigate(dest)
                                }

                                R.id.action_homeFragment_to_aboutFragment -> {
                                    findNavController().navigate(dest)
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
                        },
                        onIssuesStateChanged = { index, issuesType, issueState ->
                            val isOpen = when (issueState) {
                                IssueState.Open -> true
                                IssueState.Closed -> false
                                IssueState.All -> true
                            }
                            homeViewModel.updateIssueScreen(index, isOpen)
                            homeViewModel.getIssuesWithCount(
                                token = token,
                                query = getUrlForIssues(
                                    issuesType,
                                    issueState,
                                    state.user.data!!.login
                                ),
                                page = 1,
                                issuesType = issuesType
                            )
                        },
                        onPullsStateChanged = { index, myIssuesType, issueState ->
                            val isOpen = when (issueState) {
                                IssueState.Open -> true
                                IssueState.Closed -> false
                                IssueState.All -> true
                            }
                            homeViewModel.updatePullScreen(index, isOpen)
                            homeViewModel.getPullsWithCount(
                                token = token,
                                query = getUrlForPulls(
                                    myIssuesType,
                                    issueState,
                                    state.user.data!!.login
                                ),
                                page = 1,
                                issuesType = myIssuesType
                            )
                        },
                        scaffoldState = scaffoldState,
                        onNavigationClick = {
                            drawerScope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun getUrlForPulls(
        issueType: MyIssuesType,
        issueState: IssueState,
        login: String
    ): String {
        when (issueType) {
            MyIssuesType.CREATED -> return RepoQueryProvider.getMyIssuesPullRequestQuery(
                login,
                issueState,
                true
            )

            MyIssuesType.ASSIGNED -> return RepoQueryProvider.getAssigned(login, issueState, true)
            MyIssuesType.MENTIONED -> return RepoQueryProvider.getMentioned(login, issueState, true)
            MyIssuesType.REVIEW -> return RepoQueryProvider.getReviewRequests(login, issueState)
            else -> {}
        }
        return RepoQueryProvider.getMyIssuesPullRequestQuery(login, issueState, false)
    }

    private fun getUrlForIssues(
        issueType: MyIssuesType,
        issueState: IssueState,
        login: String
    ): String {
        when (issueType) {
            MyIssuesType.CREATED -> return RepoQueryProvider.getMyIssuesPullRequestQuery(
                login,
                issueState,
                false
            )

            MyIssuesType.ASSIGNED -> return RepoQueryProvider.getAssigned(login, issueState, false)
            MyIssuesType.MENTIONED -> return RepoQueryProvider.getMentioned(
                login,
                issueState,
                false
            )

            MyIssuesType.PARTICIPATED -> return RepoQueryProvider.getParticipated(
                login,
                issueState,
                false
            )

            else -> {}
        }
        return RepoQueryProvider.getMyIssuesPullRequestQuery(login, issueState, false)
    }

}
