package com.hasan.jetfasthub.screens.main.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.core.ui.extensions.executeWithLifecycle
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
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
                JetFastHubTheme {
                    HomeScreen(state = homeScreenViewModel.uiState)
                }
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
            HomeScreenNavigation.OpenNotificationFragment -> {}
            HomeScreenNavigation.OpenSearchFragment -> {}
            HomeScreenNavigation.OpenAboutFragment -> TODO()
            HomeScreenNavigation.OpenFaqFragment -> TODO()
            is HomeScreenNavigation.OpenGistsFragment -> TODO()
            is HomeScreenNavigation.OpenIssueFragmentFragment -> TODO()
            HomeScreenNavigation.OpenPinnedFragment -> TODO()
            is HomeScreenNavigation.OpenProfileFragment -> TODO()
            is HomeScreenNavigation.OpenRepositoryFragment -> TODO()
            HomeScreenNavigation.OpenSettingsFragment -> TODO()
        }
    }

}
