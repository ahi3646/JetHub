package com.hasan.jetfasthub.screens.main.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.executeWithLifecycle
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.GIST_DATA
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.ISSUE_NUMBER
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.ISSUE_OWNER
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.ISSUE_REPO
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.PROFILE_START_TAB_INDEX
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.PROFILE_USERNAME
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.REPOSITORY_NAME
import com.hasan.jetfasthub.core.ui.utils.NavigationConstants.REPOSITORY_OWNER
import com.hasan.jetfasthub.screens.main.home.presentation.ui.HomeScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(homeScreenViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                JetFastHubTheme { HomeScreen(state = homeScreenViewModel.uiState) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeScreenViewModel.screenNavigation.executeWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            action = ::executeNavigation
        )
    }

    private fun executeNavigation(navigation: HomeScreenNavigation) {
        when (navigation) {
            HomeScreenNavigation.OpenNotificationFragment -> {
                findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
            }
            HomeScreenNavigation.OpenSearchFragment -> {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
            HomeScreenNavigation.OpenAboutFragment -> findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
            HomeScreenNavigation.OpenFaqFragment -> findNavController().navigate(R.id.action_homeFragment_to_faqFragment)
            is HomeScreenNavigation.OpenGistsFragment -> {
                val bundle = bundleOf(GIST_DATA to navigation.username)
                findNavController().navigate(R.id.action_homeFragment_to_gistsFragment, bundle)
            }
            is HomeScreenNavigation.OpenIssueFragmentFragment -> {
                val bundle = Bundle()
                bundle.putString(ISSUE_OWNER, navigation.issueOwner)
                bundle.putString(ISSUE_REPO, navigation.issueRepo)
                bundle.putString(ISSUE_NUMBER, navigation.issueNumber)
                findNavController().navigate(R.id.action_fromFragment_to_issueFragment, bundle)
            }
            HomeScreenNavigation.OpenPinnedFragment -> findNavController().navigate(R.id.action_homeFragment_to_pinnedFragment)
            is HomeScreenNavigation.OpenProfileFragment -> {
                val bundle = Bundle()
                bundle.putString(PROFILE_USERNAME , navigation.username)
                bundle.putString(PROFILE_START_TAB_INDEX , navigation.profileTabStartIndex)
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)
            }
            is HomeScreenNavigation.OpenRepositoryFragment -> {
                val bundle = Bundle()
                bundle.putString(REPOSITORY_OWNER, navigation.repoOwner)
                bundle.putString(REPOSITORY_NAME, navigation.repoName)
                findNavController().navigate(R.id.action_homeFragment_to_repositoryFragment, bundle)
            }
            HomeScreenNavigation.OpenSettingsFragment -> findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

}
